<script lang="ts">
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';

    let title = "";
    let content = "";
    let boardCode = "free"; // 기본값: 자유게시판

    // 1. 로그인 안 한 사람 쫓아내기 (보안)
    onMount(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/members/info', { credentials: 'include' });
            if (!response.ok) {
                alert("로그인이 필요한 서비스입니다.");
                goto('/login');
            }
        } catch (e) {
            goto('/login');
        }
    });

    // 2. 글쓰기 전송 함수
    async function handleWrite() {
        if (!title || !content) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    title,
                    content,
                    boardCode
                }),
                credentials: 'include' // ✅ 쿠키(토큰) 필수!
            });

            if (response.ok) {
                alert("게시글이 등록되었습니다! 🎉");
                goto('/'); // 일단 메인으로 이동 (나중엔 쓴 글로 이동)
            } else {
                alert("글 작성 실패: " + await response.text());
            }
        } catch (error) {
            console.error(error);
            alert("서버 오류가 발생했습니다.");
        }
    }
</script>

<div class="container mt-5" style="max-width: 800px;">
    <h2 class="mb-4 fw-bold">글쓰기</h2>

    <div class="card p-4 shadow-sm">
        <form on:submit|preventDefault={handleWrite}>

            <div class="mb-3">
                <label for="boardSelect" class="form-label fw-bold">게시판 선택</label>
                <select class="form-select" id="boardSelect" bind:value={boardCode}>
                    <option value="free">자유게시판</option>
                    <option value="humor">유머게시판</option>
                </select>
            </div>

            <div class="mb-3">
                <label for="title" class="form-label fw-bold">제목</label>
                <input type="text" class="form-control" id="title" bind:value={title} placeholder="제목을 입력하세요">
            </div>

            <div class="mb-3">
                <label for="content" class="form-label fw-bold">내용</label>
                <textarea class="form-control" id="content" rows="10" bind:value={content} placeholder="내용을 입력하세요"></textarea>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="/" class="btn btn-secondary">취소</a>
                <button type="submit" class="btn btn-primary">등록하기</button>
            </div>

        </form>
    </div>
</div>