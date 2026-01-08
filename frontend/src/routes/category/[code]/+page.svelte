<script lang="ts">
    import { page } from '$app/stores';
    import { goto } from '$app/navigation';

    // 주소창에서 게시판 코드(free, humor) 가져오기
    // $: 는 Svelte에서 값이 바뀌면 자동으로 다시 실행하라는 뜻 (메뉴 이동 시 반응)
    $: boardCode = $page.params.code;
    let posts: any[] = [];
    let boardName = "";

    // boardCode가 바뀔 때마다 실행되는 함수
    $: if (boardCode) {
        loadPosts();
        boardName = boardCode === 'free' ? '자유게시판' : '유머게시판';
    }

    async function loadPosts() {
        try {
            // ✅ 백엔드에 ?boardCode=free 붙여서 요청
            const response = await fetch(`http://localhost:8080/api/posts?boardCode=${boardCode}`);
            if (response.ok) {
                posts = await response.json();
            } else {
                posts = [];
            }
        } catch (error) {
            console.error(error);
        }
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
                <tr on:click={() => goto(`/board/${post.id}`)} style="cursor: pointer;">
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