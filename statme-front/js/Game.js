const urlParams = new URLSearchParams(window.location.search);
const api = new ApiService();
let loggedInUser = false
let user
let game

document.addEventListener("DOMContentLoaded", () => {
  if (!urlParams.has('id')) {
    window.location.href = "index.html"
  }

  if (api.haveToken()) {
    showProfileButton()

    api.get('/api/profile/get',
      (data) => {
        user = data
        getGame()
      }
    )
  } else {
    getGame()
  }

  getAchievements()
})

function getGame() {
  api.get('/api/game/get?id=' + urlParams.get('id'),
    (data) => {
      fillData(data)
      game = data
      if (user && user.role.toLowerCase() === "company" && user.id === game.companyId) {
        addEditing()
      }
    }
  )
}

function getAchievements(){
  api.get('/api/game/achievement/get?id=' + urlParams.get('id'),
    (data) => {
      fillAchievements(data)
    }
  )
}

const avatar = document.getElementById("avatar")
const title = document.getElementById("title")
const company = document.getElementById("company")
const description = document.getElementById("description")

function fillData(game) {
  avatar.src = game.photo == null ? "../img/avatar.png" : game.photo
  title.innerHTML = game.title
  api.get('/api/profile/getById?id=' + game.companyId, (data) => {
    company.innerHTML = "Компания: " + data.username
  })
  description.innerHTML = game.description
}

const achievementsContainer = document.getElementById("achievements")

function fillAchievements(achievements){
  achievements.forEach(achievement => {
    const div = document.createElement("div")
    //const delButton = loggedInUser ? `<button id="delete-game-${game.id}" class="delete-game">x</button>` : ``
    div.className = "game-card"
    div.innerHTML = `<h2 style="margin-left: 1rem">${achievement.title}</h2>`
    div.innerHTML += `<h6 style="position: absolute; top: -1rem;left: 1rem;opacity:0.5">id: ${achievement.id}</h6>`
    div.onclick = function (){
      if (confirm("Удалить достижение?")){
        api.delete("/api/game/achievement/delete?id="+achievement.id, (data) => {
          window.location.reload()
        })
      }
    }
    achievementsContainer.appendChild(div)
  })
}

function addEditing() {
  document.getElementById("achievements_section").classList.add("section-title-clickable")
  document.getElementById("add_achievement_ic").style.display = "unset"

  document.getElementById("achievements_section").addEventListener("click", () => {
    document.getElementById("create-achievement-dialog").classList.remove("invisible")
  })

  document.getElementById("create-achievement-close").addEventListener("click", () => {
    document.getElementById("create-achievement-dialog").classList.add("invisible")
  })

  document.getElementById("create_achievement_form").addEventListener("submit", (e) => {
    e.preventDefault()

    const name = document.getElementById("create_achievement_name").value.trim()

    api.post('/api/game/achievement/add',
      {
        title: name,
        gameId: game.id
      },
      (data) => {
        window.location.reload()
      }
    )
  })

  document.getElementById("api").classList.remove("invisible")

  document.getElementById("copy_token").addEventListener("click", () => {
    navigator.clipboard.writeText(api.token).then(r => alert("Токен скопирован в буфер обмена"))
  })
}

function showProfileButton() {
  document.getElementById("auth").style.display = "none"
  document.getElementById("profile").style.display = "unset"
}

/*

const games = document.getElementById("games")
const fastLinks = document.getElementById("fast_links")

function addGame(game) {
  // add game card
  const div = document.createElement("div")
  const delButton = loggedInUser ? `<button id="delete-game-${game.id}" class="delete-game">x</button>` : ``
  div.className = "game-card"
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

  document.getElementById(`delete-game-${game.id}`).addEventListener("click", () => {
    if(confirm("Вы уверены?")) {
      api.delete(endpoint + game.id, (data) => {
        if (data.success) {
          alert(data.message)
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
*/
