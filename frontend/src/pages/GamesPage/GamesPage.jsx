import './GamesPage.css'
import UserProfile from '../../components/profile/UserProfile/UserProfile.jsx'
import StatsPanel from '../../components/stats/StatsPanel/StatsPanel.jsx'
import GamesFilters from '../../components/games/GamesFilters/GamesFilters.jsx'
import GamesGrid from '../../components/games/GamesGrid/GamesGrid.jsx'

function GamesPage() {
    return (
        <main className="games-page">
            <section className="games-top">
                <UserProfile />
                <StatsPanel/> {/*Dentro terá o StatCard*/}
            </section>

            <section className="games-content">
                <h2>GamesPage</h2>
                <GamesFilters/>
                <GamesGrid /> {/*Dentro terá o GameCard*/}


                <p>Essa sera a pagina principal depois do login.</p>
            </section>

        </main>
    )
}

export default GamesPage
