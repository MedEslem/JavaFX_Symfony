<?php

namespace App\Entity;


use App\Repository\EquipeRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EquipeRepository::class)]
class Equipe
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"nom is required")]
    #[Assert\Length(min:4,minMessage:"Entrer un nom au min de 4 caracteres")]
    private ?string $nom_e = null;

    #[ORM\Column]
    #[Assert\NotBlank(message:"Number Of Member is required")]
    #[Assert\Range(min : 1 , max : 7 , notInRangeMessage: "Team Has  {{ min }} et {{ max }} Members ")]
    private ?int $nb_p = null;

    public function getNbP(): ?int
    {
        return $this->nb_p;
    }

    public function setNbP(?int $nb_p): void
    {
        $this->nb_p = $nb_p;
    }

    #[ORM\ManyToOne(inversedBy: 'equipes')]
    #[ORM\JoinColumn(onDelete:"CASCADE")]
    #[Assert\NotBlank(message:"competition name is required")]
    private ?Competition $comp = null;

    #[ORM\ManyToOne(inversedBy: 'equipes')]
    private ?User $user = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomE(): ?string
    {
        return $this->nom_e;
    }

    public function setNomE(string $nom_e): static
    {
        $this->nom_e = $nom_e;

        return $this;
    }
    public function getComp(): ?Competition
    {
        return $this->comp;
    }

    public function setComp(?Competition $comp): static
    {
        $this->comp = $comp;

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