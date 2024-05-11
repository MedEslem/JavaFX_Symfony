<?php

namespace App\Entity;

use App\Repository\EventRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;




#[ORM\Entity(repositoryClass: EventRepository::class)]
class Event
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_event = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    private ?string $Titre_event = null;

    #[ORM\Column(type: Types::DECIMAL, precision: 10, scale: '0')]
    #[Assert\NotBlank(message: "champ null")]
    #[Assert\Positive(message: "Number should be positive")]
    private ?string $Prix_event = null;

    #[ORM\Column(type: Types::DECIMAL, precision: 10, scale: '0')]
    #[Assert\NotBlank(message: "champ null")]
    #[Assert\Positive(message: "Number should be positive")]
    private ?string $Nbr_personnes = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    private ?string $Description_event = null;

    #[ORM\Column(length: 255)]
    private ?\DateTimeImmutable $Date_event = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    private ?string $Etat_event = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    private ?string $Lieu_event = null;

    #[ORM\ManyToOne(inversedBy: 'events')]
    #[ORM\JoinColumn(nullable: false, referencedColumnName:"id_category",onDelete:"CASCADE")]
    private ?Category $Categorie_Event = null;

    #[ORM\OneToMany(targetEntity: Reservation::class, mappedBy: 'Event')]
    private Collection $reservations;

    public function __construct()
    {
        $this->reservations = new ArrayCollection();
    }



    public function getIdEvent(): ?int
    {
        return $this->id_event;
    }

    public function getTitreEvent(): ?string
    {
        return $this->Titre_event;
    }

    public function setTitreEvent(string $Titre_event): static
    {
        $this->Titre_event = $Titre_event;

        return $this;
    }

    public function getPrixEvent(): ?string
    {
        return $this->Prix_event;
    }

    public function setPrixEvent(string $Prix_event): static
    {
        $this->Prix_event = $Prix_event;

        return $this;
    }

    public function getNbrPersonnes(): ?string
    {
        return $this->Nbr_personnes;
    }

    public function setNbrPersonnes(string $Nbr_personnes): static
    {
        $this->Nbr_personnes = $Nbr_personnes;

        return $this;
    }

    public function getDescriptionEvent(): ?string
    {
        return $this->Description_event;
    }

    public function setDescriptionEvent(string $Description_event): static
    {
        $this->Description_event = $Description_event;

        return $this;
    }

    public function getDateEvent(): ?\DateTimeImmutable
    {
        return $this->Date_event;
    }

    public function setDateEvent(\DateTimeImmutable $Date_event): static
    {
        $this->Date_event = $Date_event;

        return $this;
    }

    public function getEtatEvent(): ?string
    {
        return $this->Etat_event;
    }

    public function setEtatEvent(string $Etat_event): static
    {
        $this->Etat_event = $Etat_event;

        return $this;
    }

    public function getLieuEvent(): ?string
    {
        return $this->Lieu_event;
    }

    public function setLieuEvent(string $Lieu_event): static
    {
        $this->Lieu_event = $Lieu_event;

        return $this;
    }

    public function getCategorieEvent(): ?Category
    {
        return $this->Categorie_Event;
    }

    public function setCategorieEvent(?Category $Categorie_Event): static
    {
        $this->Categorie_Event = $Categorie_Event;

        return $this;
    }



    /**
     * @return Collection<int, Reservation>
     */
    public function getReservations(): Collection
    {
        return $this->reservations;
    }

    public function addReservation(Reservation $reservation): static
    {
        if (!$this->reservations->contains($reservation)) {
            $this->reservations->add($reservation);
            $reservation->setEvent($this);
        }

        return $this;
    }

    public function removeReservation(Reservation $reservation): static
    {
        if ($this->reservations->removeElement($reservation)) {
            // set the owning side to null (unless already changed)
            if ($reservation->getEvent() === $this) {
                $reservation->setEvent(null);
            }
        }

        return $this;
    }


    public function __toString()
    {
        return $this->Titre_event; // ou tout autre champ que vous voulez afficher
    }



}