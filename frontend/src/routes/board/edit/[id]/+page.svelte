<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';

    const postId = $page.params.id;

    let title = "";
    let content = "";

    // 1. 기존 글 내용 가져오기
    onMount(async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/posts/${postId}`);
            if (response.ok) {
                const data = await response.json();
                title = data.title;
                content = data.content;
                // 게시판 종류(boardCode)는 수정 불가하므로 안 가져와도 됨
            } else {
                alert("글 정보를 불러오지 못했습니다.");
                goto(`/board/${postId}`);
            }
        } catch (e) {
            console.error(e);
        }
    });

    // 2. 수정 요청 보내기 (PUT)
    async function handleUpdate() {
        if (!confirm("정말 수정하시겠습니까?")) return;

        try {
            const response = await fetch(`http://localhost:8080/api/posts/${postId}`, {
                method: 'PUT', // ✅ 수정은 PUT 메서드
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, content }),
                credentials: 'include'
            });

            if (response.ok) {
                alert("수정되었습니다! ✨");
                goto(`/board/${postId}`); // 상세 페이지로 복귀
            } else {
                alert("수정 실패: " + await response.text());
            }
        } catch (error) {
            console.error(error);
            alert("서버 오류가 발생했습니다.");
        }
    }
</script>

<div class="container mt-5" style="max-width: 800px;">
    <h2 class="mb-4 fw-bold">글 수정하기</h2>

    <div class="card p-4 shadow-sm">
        <form on:submit|preventDefault={handleUpdate}>
            <div class="mb-3">
                <label for="title" class="form-label fw-bold">제목</label>
                <input type="text" class="form-control" id="title" bind:value={title}>
            </div>

            <div class="mb-3">
                <label for="content" class="form-label fw-bold">내용</label>
                <textarea class="form-control" id="content" rows="10" bind:value={content}></textarea>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="/board/{postId}" class="btn btn-secondary">취소</a>
                <button type="submit" class="btn btn-primary">수정 완료</button>
            </div>
        </form>
    </div>
</div>