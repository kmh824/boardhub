<script lang="ts">
    // 1. 로그인 함수
    async function handleLogin() {
        if (!email || !password) {
            alert("이메일과 비밀번호를 입력해주세요.");
            return;
        }

        try {
            // 2. 백엔드에 로그인 요청
            const response = await fetch('http://localhost:8080/api/members/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
                credentials: 'include' // ✅ [중요] 이게 있어야 "쿠키"를 주고받을 수 있음!
            });

            // 3. 결과 처리
            if (response.ok) {
                alert("로그인 성공! 🔑 홈으로 이동합니다.");
                // 화면을 새로고침하면서 이동 (그래야 나중에 헤더가 바뀜)
                location.href = "/";
            } else {
                alert("로그인 실패: 아이디나 비밀번호를 확인해주세요.");
            }
        } catch (error) {
            console.error(error);
            alert("서버 연결 실패 ㅠㅠ");
        }
    }

    let email = "";
    let password = "";
</script>

<div class="container mt-5" style="max-width: 400px;">
    <h2 class="mb-4 fw-bold text-center">로그인</h2>

    <div class="card p-4 shadow-sm bg-light">
        <form on:submit|preventDefault={handleLogin}>
            <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <input type="email" class="form-control" id="email" bind:value={email} placeholder="example@email.com">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" class="form-control" id="password" bind:value={password} placeholder="비밀번호">
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 mt-3">로그인</button>

            <div class="text-center mt-3">
                <a href="/signup" class="text-decoration-none">아직 회원이 아니신가요?</a>
            </div>
        </form>
    </div>
</div>