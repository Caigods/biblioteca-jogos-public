import './StatsPanel.css'
import StatCard from '../StatCard/StatCard.jsx'

function StatsPanel() {
    return (
        <section className="painel-estatisticas">
            <StatCard title="Jogos" value="24"/>
            <StatCard title="Horas jogadas" value="180h"/>
            <StatCard title="Zerados" value="12"/>
            <StatCard title="Nota média" value="8.7"/>
        </section>
    )
}

export default StatsPanel
