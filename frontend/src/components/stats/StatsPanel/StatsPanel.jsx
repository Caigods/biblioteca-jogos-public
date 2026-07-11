import './StatsPanel.css'
import StatCard from '../StatCard/StatCard.jsx'
import StatCardPlataform from '../StatCard/StatCardPlataform.jsx'

function StatsPanel() {
    return (
        <section className="painel-estatisticas">
            <StatCard title="Jogos" value="24"/>
            <StatCard title="Horas jogadas" value="180h"/>
            <StatCard title="Zerados" value="12"/>
            <StatCardPlataform title="Plataforma favorita" value="Playstation"/>
        </section>
    )
}

export default StatsPanel
