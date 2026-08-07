import './GameDetailCapa.css'
import capaTeste from '../../../../assets/capa_teste.jpg'


const capa = {

    capaUrl: capaTeste
}

function CapaGame() {
    return (
        <div className="capa-game">
            <img className="capa-game-imagem"
                 src={capa.capaUrl}
                 alt={"Capa do Jogo"}
                 />

        </div>
    )
}
export default CapaGame