<script lang="ts">
	// 방금 만든 컴포넌트 가져오기
	import Header from '$lib/components/Header.svelte';
	import Footer from '$lib/components/Footer.svelte';
</script>

<div class="d-flex flex-column min-vh-100">

	<Header />

	<main class="flex-grow-1 container mt-4">
		<slot />
	</main>

	<Footer />

</div>