<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import Comment from '$lib/components/Comment.svelte'; // ✅ 컴포넌트 불러오기

    const postId = $page.params.id;
    let post: any = null;
    let currentUserEmail = "";

    // 댓글 관련 변수
    let comments: any[] = [];
    let newComment = ""; // 새 댓글 내용

    // 데이터 불러오는 함수 (게시글 + 댓글)
    async function loadData() {
        try {
            // 1. 게시글 조회
            const postRes = await fetch(`http://localhost:8080/api/posts/${postId}`);
            if (postRes.ok) {
                post = await postRes.json();
            }

            // 2. 댓글 목록 조회 ✅ 추가
            const commentRes = await fetch(`http://localhost:8080/api/comments/${postId}`);
            if (commentRes.ok) {
                comments = await commentRes.json();
            }

            // 3. 내 정보 조회 (로그인 여부 확인용)
            const userRes = await fetch('http://localhost:8080/api/members/info', { credentials: 'include' });
            if (userRes.ok) {
                const userData = await userRes.json();
                currentUserEmail = userData.email;
            }
        } catch (e) {
            console.error(e);
        }
    }

    onMount(loadData); // 화면 켜지면 실행

    // 새 댓글 작성 함수 (최상위 댓글)
    async function handleCommentSubmit() {
        if (!newComment) return alert("내용을 입력해주세요.");

        try {
            const response = await fetch('http://localhost:8080/api/comments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: newComment,
                    postId: postId,
                    parentId: null // 최상위 댓글이므로 부모 없음
                }),
                credentials: 'include'
            });

            if (response.ok) {
                alert("댓글이 등록되었습니다. 💬");
                newComment = "";
                loadData(); // 목록 새로고침
            } else {
                alert("로그인이 필요합니다.");
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 기존 게시글 삭제 함수 등...
    async function handleDelete() { /* ... 기존과 동일 ... */ }
    function formatDate(d: string) { return new Date(d).toLocaleString(); }
</script>

<div class="container mt-5" style="max-width: 800px;">
    {#if post}
        <div class="border-bottom pb-3 mb-4">
            <h2 class="fw-bold mb-3">{post.title}</h2>
            <div class="content-box mb-5" style="min-height: 200px; white-space: pre-wrap;">
                {post.content}
            </div>
        </div>

        <div class="mt-5">
            <h5 class="fw-bold mb-3">💬 댓글 ({comments.length})</h5>

            <div class="card mb-4">
                <div class="card-body">
                    <textarea class="form-control mb-2" rows="3" placeholder="댓글을 남겨보세요..." bind:value={newComment}></textarea>
                    <div class="d-flex justify-content-end">
                        <button class="btn btn-primary btn-sm" on:click={handleCommentSubmit}>등록</button>
                    </div>
                </div>
            </div>

            <div class="d-flex flex-column gap-3">
                {#each comments as comment (comment.id)}
                    <Comment {comment} {postId} {currentUserEmail} on:refresh={loadData} />
                {:else}
                    <div class="text-center text-muted py-3">아직 댓글이 없습니다. 첫 번째 댓글을 남겨보세요!</div>
                {/each}
            </div>
        </div>
        <div class="d-flex justify-content-between border-top pt-4 mt-5">
            <a href="/" class="btn btn-secondary">목록으로</a>
            {#if post.authorEmail === currentUserEmail}
            {/if}
        </div>
    {:else}
    {/if}
</div>