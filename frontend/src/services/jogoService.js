import { apiRequest } from './api'

export function listarJogos() {
  return apiRequest('/jogos')
}

export function salvarJogo(jogo) {
  return apiRequest('/jogos', {
    method: 'POST',
    body: JSON.stringify(jogo),
  })
}

export function atualizarJogo(id, jogo) {
  return apiRequest(`/jogos/${id}`, {
    method: 'PUT',
    body: JSON.stringify(jogo),
  })
}

export function deletarJogo(id) {
  return apiRequest(`/jogos/${id}`, {
    method: 'DELETE',
  })
}

export function adicionarHorasJogadas(id, horasJogadas) {
  return apiRequest(
    `/jogos/${id}/adicionar-horas?horasJogadas=${horasJogadas}`,
    {
      method: 'PATCH',
    },
  )
}

export function atualizarStatusJogo(id, statusJogo) {
  return apiRequest(`/jogos/${id}/atualizar-status?statusJogo=${statusJogo}`, {
    method: 'PATCH',
  })
}
