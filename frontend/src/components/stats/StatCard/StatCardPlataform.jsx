import './StatCardPlataform.css'

function StatCard({ title, value }) {
  return (
      <article className="stat-card-plataform">
        <strong className="title">{title}</strong>
          <br/>
        <span className="value-plataform">{value}</span>
      </article>
  )
}

export default StatCard
