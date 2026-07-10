import { apiRequest } from './api'

export function login(credentials) {
  return apiRequest('/usuario/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
  })
}

export function cadastrarUsuario(usuario) {
  return apiRequest('/usuario', {
    method: 'POST',
    body: JSON.stringify(usuario),
  })
}
