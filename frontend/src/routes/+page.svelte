<script lang="ts">
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation'; // 페이지 이동 함수

    // 게시글 데이터를 담을 변수
    let posts: any[] = [];

    // 화면이 켜지면 백엔드에서 글 가져오기
    onMount(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/posts');
            if (response.ok) {
                posts = await response.json();
            }
        } catch (error) {
            console.error("게시글 로딩 실패:", error);
        }
    });

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

    <div class="card shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="text-center" style="width: 10%">번호</th>
                    <th scope="col" style="width: 10%">게시판</th>
                    <th scope="col" style="width: 45%">제목</th>
                    <th scope="col" class="text-center" style="width: 15%">작성자</th>
                    <th scope="col" class="text-center" style="width: 20%">작성일</th>
                </tr>
                </thead>
                <tbody>
                {#each posts as post (post.id)}
                    <tr onclick={() => goToDetail(post.id)} style="cursor: pointer;">
                        <td class="text-center">{post.id}</td>
                        <td><span class="badge bg-secondary">{post.boardName}</span></td>
                        <td class="fw-bold">{post.title}</td>
                        <td class="text-center">{post.author}</td>
                        <td class="text-center text-muted small">{formatDate(post.modifiedDate)}</td>
                    </tr>
                {:else}
                    <tr>
                        <td colspan="5" class="text-center py-5 text-muted">
                            아직 등록된 게시글이 없습니다. 📝
                        </td>
                    </tr>
                {/each}
                </tbody>
            </table>
        </div>
    </div>
</div>