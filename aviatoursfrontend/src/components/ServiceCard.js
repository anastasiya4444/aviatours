const ServiceCard = ({ service, isSelected, onSelect }) => {
    return (
      <div className={`card h-100 ${isSelected ? 'border-success' : ''}`}>
        {service.imageUrl && (
          <img 
            src={service.imageUrl} 
            className="card-img-top" 
            alt={service.serviceType}
          />
        )}
        <div className="card-body">
          <h5 className="card-title">{service.serviceType}</h5>
          <p className="card-text">{service.description || 'Описание отсутствует'}</p>
          <p className="h5">{service.cost} руб.</p>
          <button
            className={`btn w-100 ${isSelected ? 'btn-success' : 'btn-outline-primary'}`}
            onClick={onSelect}
          >
            {isSelected ? 'Выбрано' : 'Выбрать'}
          </button>
        </div>
      </div>
    );
  };