import './GameDetailsPage.css'
import UserProfile from '../../components/profile/UserProfile2/UserProfile.jsx'
import Logo from '../../components/logo/Logo.jsx'
import CapaGame from '../../components/games/GameDetails/Capa/GameDetailCapa.jsx'

function GameDetailsPage() {
    return (
        <main className="detail-page">

            <div className="detail-top">
                <div className="logo-top">
                    <Logo/>
                </div>

                <div className="profile-top">
                    <UserProfile/>
                </div>
            </div>
            <div className="buttons-line">
                <button
                    type="button"
                    className="buttons-detail-page"
                    onClick="(função deletar jogo)" /*  adicionar essa função*/
                >
                    Excluir
                </button>
                <button
                    type="button"
                    className="buttons-detail-page"
                    onClick="(função deletar jogo)" /*  adicionar essa função*/
                >
                    Editar
                </button>
                <button
                    type="button"
                    className="buttons-detail-page"
                    onClick="(função deletar jogo)" /*  adicionar essa função*/
                >
                    Adicionar Horas
                </button>

            </div>
            <section className="game-detail-divs">

                <div className="capa">
                    <CapaGame/>


                </div>
                <div className="game-stats">
                    <p> teste 2</p>

                </div>
                <div className="game-details">


                </div>
            </section>

        </main>
    )
}

export default GameDetailsPage
