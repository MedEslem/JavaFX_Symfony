<?php

namespace App\Controller;

use App\Entity\Produit;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use App\Repository\ProduitRepository;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;
/**
 * @Route("/cart", name="cart_")
 */
class CartController extends AbstractController
{
    /**
     * @Route("/", name="index")
     */
    public function index(SessionInterface $session,ProduitRepository $produitRepository): Response
    {

        $panier = $session->get("panier", []);

        // On "fabrique" les données
        $dataPanier = [];
        $total = 0;

        foreach($panier as $id_produit => $quantite){
            $product = $produitRepository->find($id_produit);
            $dataPanier[] = [
                "produit" => $product,
                "quantite" => $quantite
            ];
            $total += $product->getPrixProduit() * $quantite;
        }

        return $this->render('cart/index.html.twig', compact("dataPanier", "total"));
    }

    /**
     * @Route("/add/{id_produit}", name="add")
     */
    public function add(Produit $product, SessionInterface $session)
    {
        // On récupère le panier actuel
        $panier = $session->get("panier", []);
        $id_produit = $product->getIdProduit();

        if(!empty($panier[$id_produit])){
            $panier[$id_produit]++;
        }else{
            $panier[$id_produit] = 1;
        }

        // On sauvegarde dans la session
        $session->set("panier", $panier);
        flash()->addSuccess('This Product has been added To your CART ');

        return $this->redirectToRoute("cart_index");
    }
    /**
     * @Route("/add1/{id_produit}", name="add1")
     */
    public function add1(Produit $product, SessionInterface $session)
    {
        // On récupère le panier actuel
        $panier = $session->get("panier", []);
        $id_produit = $product->getIdProduit();

        if(!empty($panier[$id_produit])){
            $panier[$id_produit]++;
        }else{
            $panier[$id_produit] = 1;
        }

        // On sauvegarde dans la session
        $session->set("panier", $panier);
        flash()->addSuccess('This Product has been added ');

        return $this->redirectToRoute("cart_index");
    }

    /**
     * @Route("/remove/{id_produit}", name="remove")
     */
    public function remove(Produit $product, SessionInterface $session)
    {
        // On récupère le panier actuel
        $panier = $session->get("panier", []);
        $id_produit = $product->getIdProduit();

        if(!empty($panier[$id_produit])){
            if($panier[$id_produit] > 1){
                $panier[$id_produit]--;
            }else{
                unset($panier[$id_produit]);
            }
        }

        // On sauvegarde dans la session
        $session->set("panier", $panier);

        return $this->redirectToRoute("cart_index");
    }

    /**
     * @Route("/delete/{id_produit}", name="delete")
     */
    public function delete(Produit $product, SessionInterface $session)
    {
        // On récupère le panier actuel
        $panier = $session->get("panier", []);
        $id_produit = $product->getIdProduit();

        if(!empty($panier[$id_produit])){
            unset($panier[$id_produit]);
        }

        // On sauvegarde dans la session
        $session->set("panier", $panier);

        return $this->redirectToRoute("cart_index");
    }

    /**
     * @Route("/delete", name="delete_all")
     */
    public function deleteAll(SessionInterface $session)
    {
        $session->remove("panier");

        return $this->redirectToRoute("cart_index");
    }

}