const api = new ApiService()

if (api.haveToken()) {
  window.location.href = "user-profile.html"
}

document.getElementById("login_form").addEventListener('submit', function (e) {
  e.preventDefault()
  const email = document.getElementById('login_email').value.trim()
  const password = document.getElementById('login_password').value.trim()
  const message = document.getElementById('login_result')

  const body = {
    "email": email,
    "password": password
  }

  api.post('/api/auth/login', body,
    (data) => {
      if ("token" in data) {
        api.saveToken(data.token)
        message.innerHTML = "С возвращением!"
        setTimeout(() => window.location.href = "user-profile.html", 1000)
      } else {
        message.innerHTML = data.body
      }
    })
});

document.getElementById("register_form").addEventListener('submit', function (e) {
  e.preventDefault()
  const email = document.getElementById('register_email').value.trim()
  const username = document.getElementById('register_nickname').value.trim()
  const password = document.getElementById('register_password').value.trim()
  const password_c = document.getElementById('register_password_confirm').value.trim()
  const message = document.getElementById('register_result')

  if (password !== password_c) {
    message.innerHTML = "Пароли не совпадают"
    return
  }

  if (password.length < 8) {
    message.innerHTML = "Пароль должен содержать не менее 8 символов"
    return
  }

  const body = {
    "email": email,
    "username": username,
    "password": password
  }

  api.post('/api/auth/register', body,
    (data) => {
      if ("token" in data) {
        api.saveToken(data.token)
        message.innerHTML = "Добро пожаловать!"
        setTimeout(() => window.location.href = "user-profile.html", 1000)
      } else {
        message.innerHTML = data.body
      }
    })
});
