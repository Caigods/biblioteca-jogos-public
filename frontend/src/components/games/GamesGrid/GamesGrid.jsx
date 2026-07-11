import './GamesGrid.css'
import GameCard from '../GameCard/GameCard.jsx'

const jogosTeste = [
  {
    id: 1,
    tituloJogo: 'The Witcher 3',
    plataforma: 'PC',
    genero: 'RPG',
    statusJogo: 'JOGANDO',
    notaPessoal: 10,
    horasJogadas: '120h',
  },
  {
    id: 2,
    tituloJogo: 'Hollow Knight',
    plataforma: 'PC',
    genero: 'Metroidvania',
    statusJogo: 'FINALIZADO',
    notaPessoal: 9.5,
    horasJogadas: '48h',
  },
  {
    id: 3,
    tituloJogo: 'God of War',
    plataforma: 'PLAYSTATION',
    genero: 'Acao',
    statusJogo: 'FINALIZADO',
    notaPessoal: 9,
    horasJogadas: '35h',
  },
  {
    id: 4,
    tituloJogo: 'Stardew Valley',
    plataforma: 'NINTENDO_SWITCH',
    genero: 'Simulacao',
    statusJogo: 'JOGANDO',
    notaPessoal: 8.5,
    horasJogadas: '80h',
  },
  {
    id: 5,
    tituloJogo: 'Stardew Valley',
    plataforma: 'NINTENDO_SWITCH',
    genero: 'Simulacao',
    statusJogo: 'JOGANDO',
    notaPessoal: 8.5,
    horasJogadas: '80h',
  },
  {
    id: 6,
    tituloJogo: 'Stardew Valley',
    plataforma: 'NINTENDO_SWITCH',
    genero: 'Simulacao',
    statusJogo: 'JOGANDO',
    notaPessoal: 8.5,
    horasJogadas: '80h',
  },
  {
    id: 7,
    tituloJogo: 'Stardew Valley',
    plataforma: 'NINTENDO_SWITCH',
    genero: 'Simulacao',
    statusJogo: 'JOGANDO',
    notaPessoal: 8.5,
    horasJogadas: '80h',
  },
  {
    id: 8,
    tituloJogo: 'Stardew Valley',
    plataforma: 'NINTENDO_SWITCH',
    genero: 'Simulacao',
    statusJogo: 'JOGANDO',
    notaPessoal: 8.5,
    horasJogadas: '80h',
  },
]

function GamesGrid() {
  return (
    <section className="games-grid">
      {jogosTeste.map((jogo) => (
        <GameCard
          key={jogo.id}
          tituloJogo={jogo.tituloJogo}
          plataforma={jogo.plataforma}
          genero={jogo.genero}
          statusJogo={jogo.statusJogo}
          notaPessoal={jogo.notaPessoal}
          horasJogadas={jogo.horasJogadas}
        />
      ))}
    </section>
  )
}

export default GamesGrid
