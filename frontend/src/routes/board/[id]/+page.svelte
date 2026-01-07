<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';

    // 1. 주소창에서 글 번호(id) 가져오기
    const postId = $page.params.id;

    let post: any = null;

    // 2. 화면 켜지면 백엔드에서 상세 정보 가져오기
    onMount(async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/posts/${postId}`);
            if (response.ok) {
                post = await response.json();
            } else {
                alert("존재하지 않거나 삭제된 게시글입니다.");
                history.back(); // 뒤로 가기
            }
        } catch (e) {
            console.error(e);
            alert("서버 오류가 발생했습니다.");
        }
    });

    // 날짜 포맷 함수
    function formatDate(dateString: string) {
        return new Date(dateString).toLocaleString();
    }
</script>

<div class="container mt-5" style="max-width: 800px;">
    {#if post}
        <div class="border-bottom pb-3 mb-4">
            <h5 class="text-muted small mb-2">{post.boardName}</h5>
            <h2 class="fw-bold mb-3">{post.title}</h2>

            <div class="d-flex justify-content-between align-items-end text-muted">
                <div>
                    <span class="fw-bold text-dark">{post.author}</span>
                    <span class="mx-2">|</span>
                    <span class="small">{formatDate(post.modifiedDate)}</span>
                </div>
                <div class="small">
                    조회 {post.viewCount}
                </div>
            </div>
        </div>

        <div class="content-box mb-5" style="min-height: 300px; white-space: pre-wrap;">
            {post.content}
        </div>

        <div class="d-flex justify-content-center gap-2 border-top pt-4">
            <a href="/" class="btn btn-secondary px-4">목록으로</a>
        </div>
    {:else}
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    {/if}
</div>