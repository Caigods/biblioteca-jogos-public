const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export async function apiRequest(path, options = {}) {
  const token = localStorage.getItem('authToken')

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: token } : {}),
      ...options.headers,
    },
  })

  if (!response.ok) {
    throw new Error('Erro ao chamar a API')
  }

  if (response.status === 204) {
    return null
  }

  const contentType = response.headers.get('content-type')

  if (contentType?.includes('application/json')) {
    return response.json()
  }

  return response.text()
}
