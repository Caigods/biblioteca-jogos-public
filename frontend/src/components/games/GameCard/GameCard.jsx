import './GameCard.css'

function GameCard({
  tituloJogo,
  plataforma,
  genero,
  statusJogo,
  notaPessoal,
  horasJogadas,
}) {
  return (
    <article className="game-card">
      <div className="game-card-cover" aria-label="Capa do jogo">
        photo
      </div>

      <h2 className="card-game-name">{tituloJogo}</h2>

      <div className="card-game-dados">
        <p>
          <strong>Plataforma</strong>: {plataforma}
        </p>
        <p>
          <strong>Genero</strong>: {genero}
        </p>
        <p>
          <strong>Status</strong>: {statusJogo}
        </p>
        <p>
          <strong>Nota</strong>: {notaPessoal}
        </p>
        <p>
          <strong>Horas</strong>: {horasJogadas}
        </p>
      </div>
    </article>
  )
}

export default GameCard
