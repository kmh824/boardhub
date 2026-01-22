<script lang="ts">
    import { page } from '$app/stores';
    import { goto } from '$app/navigation';
    import Search from '$lib/components/Search.svelte'; // ✅ 검색 컴포넌트 추가

    // 주소창에서 게시판 코드(free, humor) 가져오기
    $: boardCode = $page.params.code;

    let posts: any[] = [];
    let boardName = "";

    // boardCode가 바뀌면 실행 (메뉴 이동 시)
    $: if (boardCode) {
        boardName = boardCode === 'free' ? '자유게시판' : '유머게시판';
        loadPosts(); // 게시판 바뀌면 목록 새로고침 (검색어 없이)
    }

    // ✅ 게시글 로딩 함수 (검색 파라미터 받도록 수정)
    async function loadPosts(searchParams = {}) {
        const { keyword, searchType } = searchParams as any;

        // ✅ 기본 URL: 검색 API 사용 + boardCode 고정!
        let url = `http://localhost:8080/api/posts/search?page=0&size=20&boardCode=${boardCode}`;

        // 검색어가 있으면 파라미터 추가
        if (keyword) {
            url += `&keyword=${encodeURIComponent(keyword)}&searchType=${searchType}`;
        }

        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                posts = data.content; // Page 객체의 content 꺼내기
            } else {
                posts = [];
            }
        } catch (error) {
            console.error(error);
        }
    }

    // ✅ 검색 이벤트 핸들러
    function handleSearchEvent(event: CustomEvent) {
        const { keyword, searchType } = event.detail;
        loadPosts({ keyword, searchType });
    }

    function formatDate(dateString: string) {
        if (!dateString) return "";
        return new Date(dateString).toLocaleDateString();
    }
</script>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="fw-bold">📂 {boardName}</h2>
        <a href="/board/write" class="btn btn-primary">✏️ 글쓰기</a>
    </div>

    <div class="d-flex justify-content-end mb-3">
        <Search on:search={handleSearchEvent} />
    </div>

    <div class="card shadow-sm">
        <table class="table table-hover mb-0">
            <thead class="table-light">
            <tr>
                <th class="text-center">번호</th>
                <th>제목</th>
                <th class="text-center">작성자</th>
                <th class="text-center">추천</th>
                <th class="text-center">조회</th>
                <th class="text-center">작성일</th>
            </tr>
            </thead>
            <tbody>
            {#each posts as post (post.id)}
                <tr onclick={() => goto(`/board/${post.id}`)} style="cursor: pointer;">
                    <td class="text-center">{post.id}</td>
                    <td class="fw-bold">{post.title}</td>
                    <td class="text-center">{post.author}</td>
                    <td class="text-center text-primary fw-bold">{post.likeCount}</td>
                    <td class="text-center text-muted small">{post.viewCount}</td>
                    <td class="text-center small">{formatDate(post.modifiedDate)}</td>
                </tr>
            {:else}
                <tr>
                    <td colspan="6" class="text-center py-5 text-muted">
                        게시글이 없습니다. 텅 비었어요! 🍃
                    </td>
                </tr>
            {/each}
            </tbody>
        </table>
    </div>
</div>