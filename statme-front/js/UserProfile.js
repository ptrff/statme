const urlParams = new URLSearchParams(window.location.search);
const api = new ApiService();
let loggedInUser = false
let role

document.addEventListener("DOMContentLoaded", () => {
  if (urlParams.has('email')) {
    // load by email
    removeEditing()
    const email = urlParams.get('email');
    api.get('/api/profile/get?email=' + email,
      (data) => {
        fillData(data)
      }
    )

  } else {
    if (!api.haveToken()) {
      // not logged in
      window.location.href = "auth.html"
    }

    //load current user
    api.get('/api/profile/get',
      (data) => {
        fillData(data)
        role = data.role
        if (data.role !== "NOT_APPROVED_COMPANY") {
          enableEditing(data)
          loggedInUser = true
        } else {
          removeEditing()
        }
      }
    )
  }
})

const avatar = document.getElementById("avatar")
const username = document.getElementById("username")
const email = document.getElementById("email")
const status = document.getElementById("status")
const editUsername = document.getElementById("edit_username")
const editStatus = document.getElementById("edit_status")
const editPhoto = document.getElementById("edit_photo")

function fillData(userdata) {
  avatar.src = userdata.photo == null ? "../img/avatar.png" : userdata.photo
  editPhoto.value = userdata.photo == null ? "" : userdata.photo
  username.innerHTML = userdata.username
  editUsername.value = userdata.username
  email.innerHTML = userdata.email
  status.innerHTML = userdata.status == null ? "" : userdata.status
  editStatus.value = userdata.status == null ? "" : userdata.status
  fillGames(userdata)

  api.get('/api/profile/achievements?email=' + userdata.email, (data) => {
    fillAchievements(data)
  })
}

function fillGames(data) {
  api.get('/api/profile/games',
    (data) => {
      data.forEach((game) => {
        addGame(game)
      })
    })
}

const games = document.getElementById("games")
const fastLinks = document.getElementById("fast_links")

function addGame(game) {
  // add game card
  const div = document.createElement("div")
  const delButton = loggedInUser ? `<button id="delete-game-${game.id}" class="delete-game">x</button>` : ``
  div.className = "game-card"
  div.onclick = () => {
    window.location.href = `game.html?id=${game.id}`
  }
  div.id = `game-${game.id}`
  div.innerHTML = `
    <div class="game-card-header">
      <img src="${game.photo}" alt=" ">
      <h2>${game.title}</h2>
      ` + delButton + `
    </div>
    <p>${game.description}</p>
  `
  games.appendChild(div)

  // add fast link
  const a = document.createElement("a")
  a.href = `#game-${game.id}`
  a.innerHTML = game.title
  fastLinks.appendChild(a)

  const endpoint = role.toLowerCase() === "company" ? "/api/game/delete?id=" : "/api/profile/edit/game/delete?id="

  document.getElementById(`delete-game-${game.id}`).addEventListener("click", (e) => {
    e.stopPropagation()
    if (confirm("Вы уверены?")) {
      api.delete(endpoint + game.id, (data) => {
        if (data.success) {
          games.removeChild(div)
          fastLinks.removeChild(a)
        } else {
          alert(data.message)
        }
      })
    }
  })
}

function enableEditing(data) {
  document.getElementById("edit_profile").addEventListener("click", () => {
    document.getElementById("edit-profile-dialog").classList.remove("invisible")
  })

  document.getElementById("avatar_change_container").addEventListener("click", () => {
    document.getElementById("edit-profile-dialog").classList.remove("invisible")
  })

  document.getElementById("edit-close").addEventListener("click", () => {
    document.getElementById("edit-profile-dialog").classList.add("invisible")
  })

  document.getElementById("logout").addEventListener("click", () => {
    api.clearToken()
    window.location.reload()
  })

  if (data.role === "USER") {
    document.getElementById("games_section").addEventListener("click", () => {
      document.getElementById("add-game-dialog").classList.remove("invisible")
    })

    document.getElementById("game-close").addEventListener("click", () => {
      document.getElementById("add-game-dialog").classList.add("invisible")
    })

    addGameSearcher()
  }

  if (data.role === "COMPANY") {
    document.getElementById("games_section").addEventListener("click", () => {
      document.getElementById("create-game-dialog").classList.remove("invisible")
    })

    document.getElementById("create-game-close").addEventListener("click", () => {
      document.getElementById("create-game-dialog").classList.add("invisible")
    })

    document.getElementById("achievements_section").style.display = "none"
    document.getElementById("achievements").style.display = "none"

    document.getElementById("create_game_form").addEventListener("submit", (e) => {
      e.preventDefault()

      const name = document.getElementById("create_game_name").value.trim()
      const description = document.getElementById("create_game_description").value.trim()
      const photo = document.getElementById("create_game_img").value.trim()

      api.post('/api/game/create',
        {
          title: name,
          description: description,
          photo: photo
        },
        (data) => {
          window.location.reload()
        }
      )
    })
  }
}

function fillAchievements(achievements) {
  const ach = document.getElementById("achievements")

  achievements.forEach((acc) => {
    const div = document.createElement("div")
    div.className = "game-card"
    div.innerHTML = `
      <div class="game-card-header">
        <h2 style="padding-left: 1rem">${acc.title}</h2>
      </div>
    `
    ach.appendChild(div)
  })
}


function removeEditing() {
  document.getElementById("avatar_change").style.display = "none"
  document.getElementById("avatar_change_container").classList.remove("avatar_hover")
  document.getElementById("games_section").classList.remove("section-title-clickable")
  document.getElementById("edit_profile").style.display = "none"
  document.getElementById("add_game_ic").style.display = "none"
  document.getElementById("logout").style.display = "none"
}

document.getElementById("edit_profile_form").addEventListener("submit", (e) => {
  e.preventDefault();

  api.post('/api/profile/edit',
    {
      username: editUsername.value.trim(),
      status: editStatus.value.trim() === "" ? null : editStatus.value.trim(),
      photo: editPhoto.value.trim() === "" ? null : editPhoto.value.trim()
    },
    (data) => {
      window.location.reload()
    }
  )
});

function addGameSearcher() {
  const gs = document.getElementById("game_search")
  gs.addEventListener('input', function (e) {
    const q = gs.value.trim()
    if (q === "") {
      clearAndFillResults([])
      return;
    }

    api.get('/api/search/games?title=' + q,
      (data) => {
        clearAndFillResults(data)
      }
    )
  })

  const gsr = document.getElementById('game_search_results');
  let chosenGame;

  function clearAndFillResults(data) {
    gsr.innerHTML = "";
    for (let i = 0; i < data.length; i++) {
      const li = document.createElement('li');
      li.className = "result-item";
      li.innerHTML = data[i].title;
      li.onclick = function () {
        chosenGame = data[i]
        fillGameInfo()
        clearAndFillResults([])
      }
      gsr.appendChild(li);
    }
  }

  function fillGameInfo() {
    document.getElementById("game-title").innerHTML = chosenGame.title
    document.getElementById("game-description").innerHTML = chosenGame.description
    document.getElementById("game-img").src = chosenGame.photo
  }

  document.getElementById("add-game-button").addEventListener("click", () => {
    if (chosenGame === undefined || chosenGame === null) {
      return
    }

    api.post('/api/profile/edit/game/add?id=' + chosenGame.id,
      {},
      (data) => {
        window.location.reload()
      }
    )
  })
}
