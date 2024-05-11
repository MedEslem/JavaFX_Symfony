<?php

namespace App\Entity;

use App\Repository\CompetitionRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CompetitionRepository::class)]
class Competition
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"nom is required")]
    #[Assert\Length(min:4,minMessage:"Entrer un nom au min de 4 caracteres")]
    private ?string $nom_c = null;


    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"description doit etre non vide !")]
    #[Assert\Length( min :10 ,minMessage :"Entrer une description au min de 10 caracteres")]
    private ?string $description_c = null;


    #[ORM\Column(type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $date_c = null;

    #[ORM\Column(type: Types::TIME_MUTABLE)]
    private ?\DateTimeInterface $heure_c = null;

    #[ORM\Column(length: 255)]
    private ?string $type_c = null;

    #[ORM\Column(length: 255)]
    private ?string $jeu_c = null;
    #[ORM\OneToMany(targetEntity: Equipe::class, mappedBy: 'comp')]
    private Collection $equipes;

    public function __construct()
    {
        $this->equipes = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomC(): ?string
    {
        return $this->nom_c;
    }

    public function setNomC(string $nom_c): static
    {
        $this->nom_c = $nom_c;

        return $this;
    }

    public function getDescriptionC(): ?string
    {
        return $this->description_c;
    }

    public function setDescriptionC(string $description_c): static
    {
        $this->description_c = $description_c;

        return $this;
    }

    public function getDateC(): ?\DateTimeInterface
    {
        return $this->date_c;
    }

    public function setDateC(\DateTimeInterface $date_c): static
    {
        $this->date_c = $date_c;

        return $this;
    }

    public function getHeureC(): ?\DateTimeInterface
    {
        return $this->heure_c;
    }

    public function setHeureC(\DateTimeInterface $heure_c): static
    {
        $this->heure_c = $heure_c;

        return $this;
    }

    public function getTypeC(): ?string
    {
        return $this->type_c;
    }

    public function setTypeC(string $type_c): static
    {
        $this->type_c = $type_c;

        return $this;
    }

    public function getJeuC(): ?string
    {
        return $this->jeu_c;
    }

    public function setJeuC(string $jeu_c): static
    {
        $this->jeu_c = $jeu_c;

        return $this;
    }


    /**
     * @return Collection<int, Equipe>
     */
    public function getEquipes(): Collection
    {
        return $this->equipes;
    }

    public function addEquipe(Equipe $equipe): static
    {
        if (!$this->equipes->contains($equipe)) {
            $this->equipes->add($equipe);
            $equipe->setComp($this);
        }

        return $this;
    }

    public function removeEquipe(Equipe $equipe): static
    {
        if ($this->equipes->removeElement($equipe)) {
            // set the owning side to null (unless already changed)
            if ($equipe->getComp() === $this) {
                $equipe->setComp(null);
            }
        }

        return $this;
    }
    public function __toString()
    {
        return $this->nom_c;
    }
}