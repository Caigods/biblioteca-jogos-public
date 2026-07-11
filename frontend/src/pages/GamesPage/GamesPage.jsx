import './GamesPage.css'
import UserProfile from '../../components/profile/UserProfile/UserProfile.jsx'
import StatsPanel from '../../components/stats/StatsPanel/StatsPanel.jsx'
import GamesFilters from '../../components/games/GamesFilters/GamesFilters.jsx'
import GamesGrid from '../../components/games/GamesGrid/GamesGrid.jsx'

function GamesPage() {
    return (
        <main className="games-page">
            <section className="games-top">
                <UserProfile/>
                <StatsPanel/> {/*Dentro terá o StatCard*/}
            </section>
            <div className="grid-content">
                <article className="search-filter">
                    <GamesFilters/>
                </article>
                <section className="games-content">
                    <h1 className="games-header">Biblioteca</h1>
                    <GamesGrid/> {/*Dentro terá o GameCard*/}
                </section>
            </div>

        </main>
    )
}

export default GamesPage
