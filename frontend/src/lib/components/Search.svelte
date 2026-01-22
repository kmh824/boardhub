<script lang="ts">
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    // ✅ [수정] 외부(부모)에서 값을 넣어줄 수 있게 export 추가
    export let keyword = '';
    export let searchType = 'title';

    function handleSearch() {
        dispatch('search', { keyword, searchType });
    }

    function handleKeydown(e: KeyboardEvent) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    }
</script>

<div class="search-container">
    <select bind:value={searchType} class="search-select form-select" style="width: auto;">
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="writer">작성자</option>
    </select>

    <input
            type="text"
            bind:value={keyword}
            on:keydown={handleKeydown}
            placeholder="검색어를 입력하세요"
            class="search-input form-control"
    />

    <button on:click={handleSearch} class="search-btn btn btn-primary">검색</button>
</div>

<style>
    .search-container {
        display: flex;
        gap: 8px;
        /* margin-bottom은 부모에서 제어하므로 제거해도 됨 */
    }
</style>