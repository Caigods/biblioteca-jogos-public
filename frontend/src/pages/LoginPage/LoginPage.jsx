import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './LoginPage.css'

function LoginPage() {
  const navigate = useNavigate()
  const [loginMessage] = useState('')

  return (
    <main className="login-page">
      <section className="auth-shell" aria-label="Acesso ao sistema">
        <form className="auth-panel" aria-label="Login">
          <div className="panel-heading">
            <span className="eyebrow">Bem-vindo de volta</span>
            <h1>Entrar</h1>
          </div>

          <label htmlFor="login-email">Email</label>
          <input id="login-email" type="email" placeholder="seu@email.com" />

          <label htmlFor="login-password">Senha</label>
          <input id="login-password" type="password" placeholder="Sua senha" />

          {loginMessage && (
            <p className="feedback-message" aria-live="polite">
              {loginMessage}
            </p>
          )}

          <button
            type="button"
            className="primary-button"
            onClick={() => navigate('/jogos')}
          >
            Entrar
          </button>
        </form>

        <form className="auth-panel" aria-label="Cadastro">
          <div className="panel-heading">
            <span className="eyebrow">Primeiro acesso</span>
            <h2>Criar conta</h2>
          </div>

          <label htmlFor="register-name">Nome</label>
          <input id="register-name" type="text" placeholder="Seu nome" />

          <label htmlFor="register-email">Email</label>
          <input id="register-email" type="email" placeholder="seu@email.com" />

          <label htmlFor="register-password">Senha</label>
          <input
            id="register-password"
            type="password"
            placeholder="Crie uma senha"
          />

          <button type="button" className="secondary-button">
            Cadastrar
          </button>
        </form>
      </section>
    </main>
  )
}

export default LoginPage
