class ApiService {
  constructor() {
    this.baseUrl = "http://localhost:8080";
    this.token = localStorage.getItem('authToken')
      ? `Bearer ${localStorage.getItem('authToken')}`
      : null;
  }

  saveToken(token) {
    localStorage.setItem('authToken', token);
    this.token = `Bearer ${token}`;
  }

  haveToken(){
    return this.token != null
  }

  clearToken() {
    this.token = null;
    localStorage.removeItem('authToken');
  }

  get(endpoint, callback) {
    const headers = this.token ? {'Authorization': this.token} : {};

    fetch(`${this.baseUrl}${endpoint}`, {
      method: 'GET',
      headers: {...headers, 'Content-Type': 'application/json'}
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json().catch(() => {
          throw new Error('Response is not valid JSON');
        });
      })
      .then(data => callback(data))
      .catch(error => console.error('GET Request Error:', error.message));
  }

  post(endpoint, body, callback) {
    const headers = this.token ? {'Authorization': this.token} : {};

    fetch(`${this.baseUrl}${endpoint}`, {
      method: 'POST',
      headers: {...headers, 'Content-Type': 'application/json'},
      body: JSON.stringify(body)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json().catch(() => {
          throw new Error('Response is not valid JSON');
        });
      })
      .then(data => callback(data))
      .catch(error => console.error('POST Request Error:', error.message));
  }

  delete(endpoint, callback) {
    const headers = this.token ? {'Authorization': this.token} : {};

    fetch(`${this.baseUrl}${endpoint}`, {
      method: 'DELETE',
      headers: {...headers, 'Content-Type': 'application/json'}
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json().catch(() => {
          throw new Error('Response is not valid JSON');
        });
      })
      .then(data => callback(data))
      .catch(error => console.error('DELETE Request Error:', error.message));
  }
}
