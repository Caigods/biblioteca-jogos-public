import './GamesFilters.css'

const statusOptions = ['JOGANDO', 'ZERADO', 'DROPADO', 'QUEUE']

const platformOptions = [
  'PC',
  'MAGNAVOX_ODYSSEY',
  'ATARI_2600',
  'COLECOVISION',
  'INTELLIVISION',
  'NES',
  'MASTER_SYSTEM',
  'ATARI_7800',
  'SNES',
  'MEGA_DRIVE',
  'NEO_GEO',
  'TURBOGRAFX_16',
  'PLAYSTATION_1',
  'NINTENDO_64',
  'SEGA_SATURN',
  'IM_3D0',
  'PLAYSTATION_2',
  'DREAMCAST',
  'GAMECUBE',
  'XBOX',
  'PLAYSTATION_3',
  'XBOX_360',
  'WII',
  'PLAYSTATION_4',
  'XBOX_ONE',
  'WII_U',
  'NINTENDO_SWITCH',
  'PLAYSTATION_5',
  'XBOX_SERIES_X',
  'XBOX_SERIES_S',
  'GAME_BOY',
  'GAME_GEAR',
  'PSP',
  'NINTENDO_DS',
  'PS_VITA',
  'NINTENDO_3DS',
]

function GamesFilters() {
  return (
    <aside className="games-filters">
      <h2 className="games-filters-title">Filtros</h2>

      <form className="games-filters-form">
        <label htmlFor="filter-title">Titulo</label>
        <input id="filter-title" type="search" placeholder="Buscar por titulo" />

        <label htmlFor="filter-genre">Genero</label>
        <input id="filter-genre" type="search" placeholder="Buscar por genero" />

        <label htmlFor="filter-min-score">Nota maior que</label>
        <input
          id="filter-min-score"
          type="number"
          min="0"
          max="10"
          step="0.5"
          placeholder="Ex: 8"
        />

        <label htmlFor="filter-status">Status</label>
        <select id="filter-status" defaultValue="">
          <option value="">Todos</option>
          {statusOptions.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>

        <label htmlFor="filter-platform">Plataforma</label>
        <select id="filter-platform" defaultValue="">
          <option value="">Todas</option>
          {platformOptions.map((platform) => (
            <option key={platform} value={platform}>
              {platform}
            </option>
          ))}
        </select>

        <button type="button" className="games-filters-button">
          Aplicar
        </button>

        <button type="button" className="games-filters-clear-button">
          Limpar
        </button>
      </form>
    </aside>
  )
}

export default GamesFilters
