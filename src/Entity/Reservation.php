<?php

namespace App\Entity;

use App\Repository\ReservationRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReservationRepository::class)]
class Reservation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_reservation = null;

    #[ORM\Column(length: 255)]
    private ?\DateTimeImmutable $Date_reservation = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    private ?string $Mode_paiement = null;

    #[ORM\ManyToOne(inversedBy: 'reservations')]
    #[ORM\JoinColumn(nullable: false, referencedColumnName:"id_event",onDelete:"CASCADE")]
    private ?Event $Event = null;

    #[ORM\ManyToOne(inversedBy: 'reservations')]
    private ?User $user = null;






    public function getIdReservation(): ?int
    {
        return $this->id_reservation;
    }


    public function getDateReservation(): ?\DateTimeImmutable
    {
        return $this->Date_reservation;
    }

    public function setDateReservation(\DateTimeImmutable $Date_reservation): static
    {
        $this->Date_reservation = $Date_reservation;

        return $this;
    }
    public function getModePaiement(): ?string
    {
        return $this->Mode_paiement;
    }

    public function setModePaiement(string $Mode_paiement): static
    {
        $this->Mode_paiement = $Mode_paiement;

        return $this;
    }

    public function getEvent(): ?Event
    {
        return $this->Event;
    }

    public function setEvent(?Event $Event): static
    {
        $this->Event = $Event;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }


}