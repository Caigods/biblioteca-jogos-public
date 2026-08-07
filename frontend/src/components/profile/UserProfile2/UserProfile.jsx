import './UserProfile.css'

const user = {
    name: 'Caigodsasdadsasdadasa', /* HardCODED, mudar para nome de usuario*/
    image: '/Icon.png',
    imageSize: 90,
}


function UserProfile() {
    return <section className="userProfile">

        <div >
            <h1 className="user_name">
                {user.name}
            </h1>
        </div>
        <img className="icone"
             src={user.image}
             alt={"Foto do usuário"}
             style={{
                 width: user.imageSize,
                 height: user.imageSize
             }}/>


    </section>

}

export default UserProfile
