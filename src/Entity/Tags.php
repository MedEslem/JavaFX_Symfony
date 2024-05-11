<?php

namespace App\Entity;

use App\Repository\TagsRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: TagsRepository::class)]
class Tags
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_tag = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"Theme is required")]
    #[Assert\Length(min:3,minMessage:"Votre Theme  ne contient pas au minimum {{ limit }} caractères.")]
    private ?string $Theme = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"Genre is required")]
    private ?string $Genre = null;

    #[ORM\OneToMany(targetEntity: Produit::class, mappedBy: 'Tags')]
    private Collection $produits;

    public function __construct()
    {
        $this->produits = new ArrayCollection();
    }


    public function getIdTag(): ?int
    {
        return $this->id_tag;
    }

    public function getTheme(): ?string
    {
        return $this->Theme;
    }

    public function setTheme(string $Theme): static
    {
        $this->Theme = $Theme;

        return $this;
    }

    public function getGenre(): ?string
    {
        return $this->Genre;
    }

    public function setGenre(string $Genre): static
    {
        $this->Genre = $Genre;

        return $this;
    }

    /**
     * @return Collection<int, Produit>
     */
    public function getProduits(): Collection
    {
        return $this->produits;
    }

    public function addProduit(Produit $produit): static
    {
        if (!$this->produits->contains($produit)) {
            $this->produits->add($produit);
            $produit->setTags($this);
        }

        return $this;
    }

    public function removeProduit(Produit $produit): static
    {
        if ($this->produits->removeElement($produit)) {
            // set the owning side to null (unless already changed)
            if ($produit->getTags() === $this) {
                $produit->setTags(null);
            }
        }

        return $this;
    }
    public function __toString()
    {
        return $this->Theme; // ou tout autre champ que vous voulez afficher
    }
}