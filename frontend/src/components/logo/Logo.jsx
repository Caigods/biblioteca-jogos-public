import { Link } from 'react-router-dom'
import logoYgc from '../../assets/logo-ygc.png'
import './Logo.css'

function Logo() {
  return (
    <Link className="app-logo" to="/jogos" aria-label="Voltar para pagina principal">
      <img className="app-logo__image" src={logoYgc} alt="YGC" />
        <p className="app-logo-sigla"><strong>YGC</strong></p>
    </Link>
  )
}

export default Logo
