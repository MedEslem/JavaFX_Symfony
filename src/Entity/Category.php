<?php

namespace App\Entity;

use App\Repository\CategoryRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;


#[ORM\Entity(repositoryClass: CategoryRepository::class)]
class Category
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_category = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    #[Assert\Length(
        min: 4,
        minMessage: 'Description event must be at least {{ limit }} characters long',
        maxMessage: 'Description event cannot be longer than {{ limit }} characters',
    )]
    private ?string $genre = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "champ null")]
    #[Assert\Length(
        min: 4,
        minMessage: 'Description event must be at least {{ limit }} characters long',
        maxMessage: 'Description event cannot be longer than {{ limit }} characters',
    )]
    private ?string $theme = null;

    #[ORM\OneToMany(targetEntity: Event::class, mappedBy: 'Categorie_Event')]
    private Collection $events;

    public function __construct()
    {
        $this->events = new ArrayCollection();
    }


    public function getIdCategory(): ?int
    {
        return $this->id_category;
    }

    public function getGenre(): ?string
    {
        return $this->genre;
    }

    public function setGenre(string $genre): static
    {
        $this->genre = $genre;

        return $this;
    }

    public function getTheme(): ?string
    {
        return $this->theme;
    }

    public function setTheme(string $theme): static
    {
        $this->theme = $theme;

        return $this;
    }

    /**
     * @return Collection<int, Event>
     */
    public function getEvents(): Collection
    {
        return $this->events;
    }

    public function addEvent(Event $event): static
    {
        if (!$this->events->contains($event)) {
            $this->events->add($event);
            $event->setCategorieEvent($this);
        }

        return $this;
    }

    public function removeEvent(Event $event): static
    {
        if ($this->events->removeElement($event)) {
            // set the owning side to null (unless already changed)
            if ($event->getCategorieEvent() === $this) {
                $event->setCategorieEvent(null);
            }
        }

        return $this;
    }
    public function __toString()
    {
        return $this->theme; // ou tout autre champ que vous voulez afficher
    }



}