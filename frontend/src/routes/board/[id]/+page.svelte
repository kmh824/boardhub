<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import Comment from '$lib/components/Comment.svelte';

    const postId = $page.params.id;
    let post: any = null;
    let currentUserEmail = "";

    // 댓글 관련 변수
    let comments: any[] = [];
    let newComment = "";

    // 추천 관련 변수
    let isLiked = false;
    let likeCount = 0;

    // 데이터 불러오기
    async function loadData() {
        try {
            // 1. 게시글 조회
            const postRes = await fetch(`http://localhost:8080/api/posts/${postId}`);
            if (postRes.ok) {
                post = await postRes.json();
                likeCount = post.likeCount; // 추천수 초기화
            }

            // 2. 댓글 목록 조회
            const commentRes = await fetch(`http://localhost:8080/api/comments/${postId}`);
            if (commentRes.ok) {
                comments = await commentRes.json();
            }

            // 3. 내 정보 조회
            const userRes = await fetch('http://localhost:8080/api/members/info', { credentials: 'include' });
            if (userRes.ok) {
                const userData = await userRes.json();
                currentUserEmail = userData.email;

                // 4. 내 추천 여부 확인 (로그인 시에만)
                checkLikeStatus();
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 내 추천 상태 확인
    async function checkLikeStatus() {
        try {
            const res = await fetch(`http://localhost:8080/api/posts/${postId}/like`, { credentials: 'include' });
            if (res.ok) {
                isLiked = await res.json();
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 추천 버튼 클릭
    async function handleLike() {
        if (!currentUserEmail) {
            alert("로그인이 필요합니다.");
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/api/posts/${postId}/like`, {
                method: 'POST',
                credentials: 'include'
            });

            if (res.ok) {
                isLiked = !isLiked;
                if (isLiked) likeCount++;
                else likeCount--;
            } else {
                alert("오류가 발생했습니다.");
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 게시글 삭제 함수
    async function handleDelete() {
        if (!confirm("정말 삭제하시겠습니까? 🗑️")) return;

        try {
            const response = await fetch(`http://localhost:8080/api/posts/${postId}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (response.ok) {
                alert("삭제되었습니다.");
                goto('/');
            } else {
                alert("삭제 실패: " + await response.text());
            }
        } catch (e) {
            alert("오류 발생");
        }
    }

    // 댓글 작성 함수
    async function handleCommentSubmit() {
        if (!newComment) return alert("내용을 입력해주세요.");

        try {
            const response = await fetch('http://localhost:8080/api/comments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: newComment,
                    postId: postId,
                    parentId: null
                }),
                credentials: 'include'
            });

            if (response.ok) {
                alert("댓글이 등록되었습니다. 💬");
                newComment = "";
                loadData();
            } else {
                alert("로그인이 필요합니다.");
            }
        } catch (e) {
            console.error(e);
        }
    }

    onMount(loadData);
    function formatDate(d: string) { return new Date(d).toLocaleString(); }
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

        <div class="content-box mb-5" style="min-height: 200px; white-space: pre-wrap;">
            {post.content}
        </div>

        <div class="d-flex justify-content-center mb-5">
            <button class="btn btn-lg d-flex align-items-center gap-2 {isLiked ? 'btn-primary' : 'btn-outline-primary'}"
                    on:click={handleLike}>
                <span>👍</span>
                <span>추천 {likeCount}</span>
            </button>
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
                    <div class="text-center text-muted py-3">아직 댓글이 없습니다.</div>
                {/each}
            </div>
        </div>

        <div class="d-flex justify-content-between border-top pt-4 mt-5">
            <a href="/" class="btn btn-secondary px-4">목록으로</a>

            {#if post.authorEmail === currentUserEmail}
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