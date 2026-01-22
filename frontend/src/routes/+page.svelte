<script lang="ts">
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import Search from '$lib/components/Search.svelte'; // ✅ 검색 컴포넌트 임포트

    // 게시글 데이터를 담을 변수
    let posts: any[] = [];

    // ✅ 게시글 불러오기 (검색 조건이 있으면 API에 파라미터 전달)
    async function fetchPosts(searchParams = {}) {
        const { keyword, searchType } = searchParams as any;

        // 기본 URL: 검색 API 사용 (페이징은 일단 0페이지 20개로 고정)
        let url = 'http://localhost:8080/api/posts/search?page=0&size=20';

        // 검색어가 있다면 파라미터 추가
        if (keyword) {
            url += `&keyword=${encodeURIComponent(keyword)}&searchType=${searchType}`;
        }

        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                // ✅ 중요: Search API는 Page 객체를 반환하므로 .content에서 리스트를 꺼내야 함
                posts = data.content;
            } else {
                console.error("게시글 로딩 실패");
            }
        } catch (error) {
            console.error("에러 발생:", error);
        }
    }

    // 화면이 켜지면 최초 실행 (검색어 없이)
    onMount(() => {
        fetchPosts();
    });

    // ✅ Search 컴포넌트에서 검색 이벤트가 발생하면 실행될 함수
    function handleSearchEvent(event: CustomEvent) {
        const { keyword, searchType } = event.detail;
        // API 다시 호출
        fetchPosts({ keyword, searchType });
    }

    // 날짜 포맷 함수
    function formatDate(dateString: string) {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toLocaleDateString();
    }

    // 상세 페이지 이동 함수
    function goToDetail(id: number) {
        goto(`/board/${id}`);
    }
</script>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="fw-bold">📢 전체 게시글</h2>
        <a href="/board/write" class="btn btn-primary">✏️ 글쓰기</a>
    </div>

    <div class="d-flex justify-content-end mb-3">
        <Search on:search={handleSearchEvent} />
    </div>

    <div class="card shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="text-center" style="width: 8%">번호</th>
                    <th scope="col" class="text-center" style="width: 10%">게시판</th>
                    <th scope="col" style="width: 40%">제목</th>
                    <th scope="col" class="text-center" style="width: 12%">작성자</th>

                    <th scope="col" class="text-center" style="width: 8%">추천</th>
                    <th scope="col" class="text-center" style="width: 8%">조회</th>

                    <th scope="col" class="text-center" style="width: 14%">작성일</th>
                </tr>
                </thead>
                <tbody>
                {#each posts as post (post.id)}
                    <tr onclick={() => goToDetail(post.id)} style="cursor: pointer;">
                        <td class="text-center">{post.id}</td>
                        <td class="text-center"><span class="badge bg-secondary">{post.boardName}</span></td>

                        <td class="fw-bold text-truncate" style="max-width: 200px;">
                            {post.title}
                        </td>

                        <td class="text-center">{post.author}</td>

                        <td class="text-center text-primary fw-bold">{post.likeCount}</td>
                        <td class="text-center text-muted small">{post.viewCount}</td>

                        <td class="text-center text-muted small">{formatDate(post.modifiedDate)}</td>
                    </tr>
                {:else}
                    <tr>
                        <td colspan="7" class="text-center py-5 text-muted">
                            조건에 맞는 게시글이 없습니다. 🕵️‍♂️
                        </td>
                    </tr>
                {/each}
                </tbody>
            </table>
        </div>
    </div>
</div>