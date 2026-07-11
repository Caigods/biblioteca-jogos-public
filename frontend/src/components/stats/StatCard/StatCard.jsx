import './StatCard.css'

function StatCard({ title, value }) {
  return (
      <article className="stat-card">
        <strong className="title">{title}</strong>
          <br/>
        <span className="value">{value}</span>
      </article>
  )
}

export default StatCard
