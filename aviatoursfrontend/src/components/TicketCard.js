const TicketCard = ({ ticket, isSelected, onSelect }) => {
    return (
      <div className={`card mb-3 ${isSelected ? 'border-primary' : ''}`}>
        <div className="card-body">
          <div className="d-flex justify-content-between">
            <div>
              <h5 className="card-title">{ticket.flightNumber}</h5>
              <p className="card-text">
                {ticket.departureAirportCode} → {ticket.arrivalAirportCode}
              </p>
              <p className="card-text">
                <small className="text-muted">
                  {new Date(ticket.departureTime).toLocaleString()} - {new Date(ticket.arrivalTime).toLocaleString()}
                </small>
              </p>
            </div>
            <div className="text-end">
              <p className="h5">{ticket.cost} руб.</p>
              <p>Место: {ticket.seatNumber}</p>
              <span className={`badge ${ticket.status === 'AVAILABLE' ? 'bg-success' : 'bg-warning'}`}>
                {ticket.status}
              </span>
            </div>
          </div>
          <button 
            className={`btn btn-sm ${isSelected ? 'btn-danger' : 'btn-primary'}`}
            onClick={onSelect}
          >
            {isSelected ? 'Убрать' : 'Выбрать'}
          </button>
        </div>
      </div>
    );
  };