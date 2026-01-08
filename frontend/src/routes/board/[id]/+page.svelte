<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation'; // 삭제 후 이동 위해 필요

    const postId = $page.params.id;
    let post: any = null;
    let currentUserEmail = ""; // 로그인한 사람 이메일

    onMount(async () => {
        try {
            // 1. 게시글 정보 가져오기
            const postRes = await fetch(`http://localhost:8080/api/posts/${postId}`);
            if (postRes.ok) {
                post = await postRes.json();
            }

            // 2. 내 정보(로그인 정보) 가져오기 -> 본인 확인용
            const userRes = await fetch('http://localhost:8080/api/members/info', { credentials: 'include' });
            if (userRes.ok) {
                const userData = await userRes.json();
                currentUserEmail = userData.email;
            }
        } catch (e) {
            console.error(e);
        }
    });

    // ... formatDate 함수 등 기존 코드 유지 ...
    function formatDate(dateString: string) {
        return new Date(dateString).toLocaleString();
    }

    // 3. 삭제 함수
    async function handleDelete() {
        if (!confirm("정말 삭제하시겠습니까? 🗑️")) return;

        try {
            const response = await fetch(`http://localhost:8080/api/posts/${postId}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (response.ok) {
                alert("삭제되었습니다.");
                goto('/'); // 메인으로 이동
            } else {
                alert("삭제 실패: " + await response.text());
            }
        } catch (e) {
            alert("오류 발생");
        }
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
            {#if post && post.authorEmail === currentUserEmail}
                <div class="d-flex gap-2">
                    <a href="/board/edit/{postId}" class="btn btn-outline-primary">수정</a>
                    <button class="btn btn-outline-danger" on:click={handleDelete}>삭제</button>
                </div>
            {/if}
        </div>
    {:else}
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    {/if}
</div>