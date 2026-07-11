import './UserProfile.css'

const user = {
    name: 'Caigods', /* HardCODED, mudar para nome de usuario*/
    image: '/Icon.png',
    imageSize: 90,
}


function UserProfile() {
    return <section className="userProfile">

        <img className="icone"
             src={user.image}
             alt={"Foto do usuário"}
             style={{
                 width: user.imageSize,
                 height: user.imageSize
             }}/>

        <div >
            <h1 className="user_name">
                         {user.name}
            </h1>
        </div>

    </section>

}

export default UserProfile
