const api = new ApiService();

const search = document.getElementById("search")
search.addEventListener('input', function (e) {
  const q = search.value.trim()
  if (q === "") {
    hideResults()
    return;
  }

  api.get('/api/search/users?username=' + q,
    (data) => {
      data.length === 0 ? hideResults() : showResults()
      clearAndFillResults(data)
    }
  )
})

const searchResults = document.getElementById('search_results');

function clearAndFillResults(data) {
  searchResults.innerHTML = "";
  for (let i = 0; i < data.length; i++) {
    const li = document.createElement('li');
    li.className = "result-item";
    li.innerHTML = data[i].username;
    li.onclick = function () {
      window.location.href = "user-profile.html?email=" + data[i].email
    }
    searchResults.appendChild(li);
  }
}

const resultsContainer = document.querySelector('.search-container');

function showResults() {
  resultsContainer.classList.add('showing-results');
}

function hideResults() {
  resultsContainer.classList.remove('showing-results');
}
