package in.kamranali.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
@NoArgsConstructor
public class Ingredient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Get the ID from DB upon persistence 
	private Long id;
	
	private String description;
	private BigDecimal amount;
	
	@ManyToOne // No cascading because if we delete an ingredient we don't want it to delete recipe. 
	private Recipe recipe;
	
	@OneToOne(fetch = FetchType.EAGER) // One to One fetch type is already eager
	private UnitOfMeasure uom;

	public Ingredient(String description, BigDecimal amount, UnitOfMeasure unitOfmeasure) {
		this.description = description;
		this.amount = amount;
		this.uom = unitOfmeasure;
	}
	
	
}
