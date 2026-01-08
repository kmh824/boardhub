<script lang="ts">
    import { onMount } from 'svelte';

    // 사용자 정보를 담을 변수 (null이면 로그인 안 함)
    let user: { nickname: string } | null = null;

    // 1. 화면이 켜지자마자 "내 정보" 가져오기
    onMount(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/members/info', {
                credentials: 'include' // ✅ 쿠키(토큰) 실어 보내기
            });

            if (response.ok) {
                // 로그인 성공 시 정보 저장
                user = await response.json();
            }
        } catch (e) {
            console.log("로그인 상태가 아닙니다.");
        }
    });

    // 2. 로그아웃 함수
    async function handleLogout() {
        if (!confirm("로그아웃 하시겠습니까?")) return;

        await fetch('http://localhost:8080/api/members/logout', {
            method: 'POST',
            credentials: 'include'
        });

        alert("로그아웃 되었습니다.");
        location.href = "/"; // 메인으로 새로고침 이동
    }
</script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand fw-bold" href="/">BoardHub</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="/category/free">자유게시판</a></li>
                <li class="nav-item"><a class="nav-link" href="/category/humor">유머게시판</a></li>
            </ul>

            <ul class="navbar-nav align-items-center">
                {#if user}
                    <li class="nav-item me-3 text-light">
                        <span class="fw-bold text-warning">{user.nickname}</span>님 환영합니다!
                    </li>
                    <li class="nav-item">
                        <button class="btn btn-outline-light btn-sm" on:click={handleLogout}>로그아웃</button>
                    </li>
                {:else}
                    <li class="nav-item"><a class="nav-link" href="/login">로그인</a></li>
                    <li class="nav-item"><a class="nav-link" href="/signup">회원가입</a></li>
                {/if}
            </ul>
        </div>
    </div>
</nav>