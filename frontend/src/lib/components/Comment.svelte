<script lang="ts">
    import { createEventDispatcher } from 'svelte';

    export let comment: any;
    export let postId: number;
    export let currentUserEmail: string = "";
    export let depth: number = 1; // 현재 깊이

    const dispatch = createEventDispatcher();

    let showReplyForm = false;
    let replyContent = "";

    // 1. 답글 달기
    async function handleReply() {
        if (!replyContent) return alert("내용을 입력해주세요.");

        try {
            const response = await fetch('http://localhost:8080/api/comments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: replyContent,
                    postId: postId,
                    parentId: comment.id
                }),
                credentials: 'include'
            });

            if (response.ok) {
                alert("답글이 등록되었습니다. ↳");
                replyContent = "";
                showReplyForm = false;
                dispatch('refresh');
            } else {
                alert("로그인이 필요하거나 오류가 발생했습니다.");
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 2. 댓글 삭제
    async function handleDelete() {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        try {
            const response = await fetch(`http://localhost:8080/api/comments/${comment.id}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (response.ok) {
                alert("삭제되었습니다.");
                dispatch('refresh');
            } else {
                alert("삭제 권한이 없습니다.");
            }
        } catch (e) {
            console.error(e);
        }
    }
</script>

<div class="card w-100 shadow-sm border-0 bg-light">
    <div class="card-body p-3">
        <div class="d-flex justify-content-between align-items-center mb-2">
            <div>
                <strong class="me-2">{comment.author}</strong>
                <span class="text-muted small">{comment.createdDate}</span>
            </div>
            {#if currentUserEmail === comment.authorEmail}
                <button class="btn btn-sm btn-link text-danger p-0 text-decoration-none" on:click={handleDelete}>삭제</button>
            {/if}
        </div>

        <p class="card-text mb-2" style="white-space: pre-wrap;">{comment.content}</p>

        <button class="btn btn-sm btn-link text-secondary p-0 text-decoration-none"
                on:click={() => showReplyForm = !showReplyForm}>
            {showReplyForm ? '취소' : '↳ 답글 달기'}
        </button>

        {#if showReplyForm}
            <div class="mt-2 d-flex gap-2">
                <input type="text" class="form-control form-control-sm" placeholder="답글을 입력하세요..." bind:value={replyContent}>
                <button class="btn btn-sm btn-secondary" on:click={handleReply}>등록</button>
            </div>
        {/if}
    </div>
</div>

{#if comment.children && comment.children.length > 0}
    <div class={depth < 3 ? "ms-4 mt-2 border-start ps-3 border-3" : "mt-2 border-top pt-2"}>
        {#each comment.children as child (child.id)}
            <svelte:self
                    comment={child}
                    {postId}
                    {currentUserEmail}
                    depth={depth + 1}
                    on:refresh
            />
        {/each}
    </div>
{/if}