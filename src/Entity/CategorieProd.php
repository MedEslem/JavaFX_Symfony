<?php

namespace App\Entity;

use App\Repository\CategorieProdRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CategorieProdRepository::class)]
class CategorieProd
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_categProd = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"nom is required")]
    #[Assert\Length(min:3,minMessage:"Votre mot  de passe ne contient pas {{ limit }} caractères.")]
    private ?string $nom_categP = null;

    #[ORM\Column(length: 255)]
    private ?string $image_categ = null;

    #[ORM\OneToMany(targetEntity: Produit::class, mappedBy: 'categorieProd')]
    private Collection $produits;

    public function __construct()
    {
        $this->produits = new ArrayCollection();
    }


    public function getIdCategP(): ?int
    {
        return $this->id_categProd;
    }

    public function getNomCategP(): ?string
    {
        return $this->nom_categP;
    }

    public function setNomCategP(string $nom_categP): static
    {
        $this->nom_categP = $nom_categP;

        return $this;
    }

    public function getImageCateg(): ?string
    {
        return $this->image_categ;
    }

    public function setImageCateg(string $image_categ): static
    {
        $this->image_categ = $image_categ;

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
            $produit->setCategorieProd($this);
        }

        return $this;
    }

    public function removeProduit(Produit $produit): static
    {
        if ($this->produits->removeElement($produit)) {
            // set the owning side to null (unless already changed)
            if ($produit->getCategorieProd() === $this) {
                $produit->setCategorieProd(null);
            }
        }

        return $this;
    }
    public function __toString()
    {
        return $this->nom_categP; // ou tout autre champ que vous voulez afficher
    }


}