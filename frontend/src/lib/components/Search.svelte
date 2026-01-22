<script lang="ts">
    import { createEventDispatcher } from 'svelte';

    // 부모(메인 페이지)에게 "검색 버튼 눌렸어!"라고 알리는 무전기
    const dispatch = createEventDispatcher();

    let keyword = '';
    let searchType = 'title'; // 기본값: 제목 검색

    // 검색 버튼 클릭 시 실행
    function handleSearch() {
        // 부모에게 검색어와 타입을 전달 (무전 침)
        dispatch('search', { keyword, searchType });
    }

    // 엔터키 쳐도 검색되게 하기
    function handleKeydown(e: KeyboardEvent) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    }
</script>

<div class="search-container">
    <select bind:value={searchType} class="search-select">
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="writer">작성자</option>
    </select>

    <input
            type="text"
            bind:value={keyword}
            on:keydown={handleKeydown}
            placeholder="검색어를 입력하세요"
            class="search-input"
    />

    <button on:click={handleSearch} class="search-btn">검색</button>
</div>

<style>
    /* 간단한 스타일링 (원하는 대로 수정 가능) */
    .search-container {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
        justify-content: flex-end; /* 오른쪽 정렬 */
    }
    .search-select, .search-input, .search-btn {
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
    .search-btn {
        background-color: #3b82f6; /* 파란색 */
        color: white;
        cursor: pointer;
        border: none;
    }
    .search-btn:hover {
        background-color: #2563eb;
    }
</style>