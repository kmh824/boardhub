<script lang="ts">
    import { goto } from '$app/navigation'; // 페이지 이동 도구

    // 1. 사용자 입력을 받을 변수들
    let email = '';
    let password = '';
    let username = '';
    let nickname = '';

    // 2. 회원가입 버튼 클릭 시 실행될 함수
    async function handleSignup() {
        // 간단한 유효성 검사
        if (!email || !password || !username || !nickname) {
            alert('모든 필드를 입력해주세요.');
            return;
        }

        try {
            // 3. 백엔드(Spring Boot)로 데이터 전송
            const response = await fetch('http://localhost:8080/api/members/join', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email,
                    password,
                    username,
                    nickname
                })
            });

            // 4. 응답 처리
            if (response.ok) {
                alert('회원가입 성공! 🎉 로그인 페이지로 이동합니다.');
                goto('/login'); // 성공 시 로그인 화면으로 이동
            } else {
                // 백엔드에서 보낸 에러 메시지 읽기 (예: "이미 가입된 이메일입니다.")
                const errorText = await response.text();
                alert('가입 실패: ' + errorText);
            }
        } catch (error) {
            console.error(error);
            alert('서버 연결에 실패했습니다. 백엔드가 켜져 있는지 확인해주세요.');
        }
    }
</script>

<div class="container mt-5" style="max-width: 500px;">
    <h2 class="mb-4 fw-bold text-center">회원가입</h2>

    <div class="card p-4 shadow-sm bg-light">
        <form on:submit|preventDefault={handleSignup}>
            <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <input type="email" class="form-control" id="email" bind:value={email} placeholder="example@email.com">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" class="form-control" id="password" bind:value={password} placeholder="비밀번호 입력">
            </div>

            <div class="mb-3">
                <label for="username" class="form-label">이름 (실명)</label>
                <input type="text" class="form-control" id="username" bind:value={username} placeholder="홍길동">
            </div>

            <div class="mb-3">
                <label for="nickname" class="form-label">닉네임</label>
                <input type="text" class="form-control" id="nickname" bind:value={nickname} placeholder="개구리">
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 mt-3">가입하기</button>
        </form>
    </div>
</div>