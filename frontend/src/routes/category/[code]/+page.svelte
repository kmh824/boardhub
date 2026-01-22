<script lang="ts">
    import { page } from '$app/stores'; // ✅ 현재 URL 정보를 담고 있는 스토어
    import { goto } from '$app/navigation'; // ✅ URL 이동 함수
    import { onMount } from 'svelte';
    import Search from '$lib/components/Search.svelte';

    // 1. URL에서 필요한 정보들을 실시간으로 읽어옵니다. (반응형 $:)
    $: boardCode = $page.params.code;
    $: keyword = $page.url.searchParams.get('keyword') || '';     // URL에 ?keyword= 가 있으면 가져옴
    $: searchType = $page.url.searchParams.get('searchType') || 'title';

    let posts: any[] = [];
    let boardName = "";

    // 2. URL 정보(boardCode, keyword 등)가 바뀔 때마다 자동으로 실행됨! ✨
    $: if (boardCode) {
        boardName = boardCode === 'free' ? '자유게시판' : '유머게시판';
        // URL에 있는 최신 keyword와 searchType으로 데이터 요청
        loadPosts(keyword, searchType);
    }

    // 데이터 가져오는 함수 (이제 파라미터를 직접 받아서 씁니다)
    async function loadPosts(currentKeyword: string, currentType: string) {
        // 기본 URL
        let url = `http://localhost:8080/api/posts/search?page=0&size=20&boardCode=${boardCode}`;

        // 검색어가 있으면 URL에 붙임
        if (currentKeyword) {
            url += `&keyword=${encodeURIComponent(currentKeyword)}&searchType=${currentType}`;
        }

        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                posts = data.content;
            } else {
                posts = [];
            }
        } catch (error) {
            console.error(error);
        }
    }

    // 3. ✅ [핵심] 검색 버튼을 누르면 '데이터 요청'이 아니라 '주소 이동'을 합니다.
    function handleSearchEvent(event: CustomEvent) {
        const { keyword: newKeyword, searchType: newType } = event.detail;

        // 현재 주소(pathname) 뒤에 쿼리 파라미터(?keyword=...)를 붙여서 이동
        // `replaceState: false`로 해야 뒤로가기 기록이 남습니다 (기본값)
        goto(`?keyword=${newKeyword}&searchType=${newType}`, { keepFocus: true });
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

    <div class="d-flex justify-content-end mb-3">
        <Search {keyword} {searchType} on:search={handleSearchEvent} />
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
                <tr onclick={() => goto(`/board/${post.id}`)} style="cursor: pointer;">
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