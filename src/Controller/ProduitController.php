<?php

namespace App\Controller;

use Dompdf\Dompdf;
use Dompdf\Options;
use App\Entity\Produit;
use App\Repository\ProduitRepository;
use App\Form\ProduitType;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\HttpFoundation\JsonResponse;






#[Route('/produit')]
class ProduitController extends AbstractController
{
    #[Route('/', name: 'app_produit_index', methods: ['GET'])]
    public function index(ProduitRepository $produitRepository): Response
    {


        return $this->render('produit/index.html.twig', [
            'produits' => $produitRepository->findAll(),

        ]);
    }

    #[Route('/new', name: 'app_produit_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {

        $produit = new Produit();
        $form = $this->createForm(ProduitType::class, $produit);
        $form->handleRequest($request);


        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('image_produit')->getData();
            if ($file) {
                // Generate a unique name for the file before saving it
                $fileName = md5(uniqid()).'.'.$file->guessExtension();


                // Move the file to the directory where brochures are stored
                $targetDirectory = $this->getParameter('kernel.project_dir') . '/public';
                $file->move(
                    $targetDirectory,
                    $fileName
                );
                $produit->setImageProduit($fileName);
            }
            $entityManager->persist($produit);
            $entityManager->flush();
            flash()->addSuccess('Product Added Successfully');
            return $this->redirectToRoute('app_produit_index', [], Response::HTTP_SEE_OTHER);
        }
        return $this->renderForm('produit/new.html.twig', [
            'produit' => $produit,
            'form' => $form,
        ]);
    }
    #[Route('/print', name: 'app_produit_print', methods: ['GET'])]
    public function print( ProduitRepository $produitRepository)
    {

        $result = $produitRepository->findAll();
        $pdfOptions = new Options();

        // Instantiate Dompdf with our options
        $dompdf = new Dompdf($pdfOptions);

        // Retrieve the HTML generated in our twig file
        $html = $this->renderView('produit/print.html.twig', [
            'produits' => $result
        ]);

        // Load HTML to Dompdf
        $dompdf->loadHtml($html);
        // (Optional) Setup the paper size and orientation 'portrait' or 'portrait'
        $dompdf->setPaper('A4', 'portrait');

        // Render the HTML as PDF
        $dompdf->render();
        // Output the generated PDF as a response with Content-Type set to 'application/pdf'
        return new Response($dompdf->output(), Response::HTTP_OK, [
            'Content-Type' => 'application/pdf',
        ]);

    }
    #[Route('/statistiques', name: 'app_produit_statistiques', methods: ['GET'])]
    public function statistiques(ManagerRegistry $managerRegistry): Response
    {
        $entityManager = $managerRegistry->getManager();

        $statistiques = $entityManager->getRepository(Produit::class)
            ->createQueryBuilder('p')
            ->select('c.nom_categP as categorie, t.Theme as tag, COUNT(p.id_produit) as totalProduits')
            ->leftJoin('p.categorieProd', 'c')
            ->leftJoin('p.Tags', 't')
            ->groupBy('c.nom_categP, t.Theme')
            ->getQuery()
            ->getResult();

        return $this->render('produit/stat.html.twig', [
            'statistiques' => $statistiques,
        ]);
    }

    #[Route('/dql', name: 'dql', methods: ['POST'])]//recherche avec dql
    public function dql(EntityManagerInterface $em, Request $request, ProduitRepository $repo):Response
    {
        $result=$repo->findAll();
        $req=$em->createQuery("select d from App\Entity\Produit d where d.prix_produit=:n OR d.categorieProd =:n OR d.nom_produit=:n OR d.id_produit=:n");
        if($request->isMethod('post'))
        {
            $value=$request->get('test');
            $req->setParameter('n',$value);
            $result=$req->getResult();

        }

        return $this->render('produit/index.html.twig',[
            'produits'=>$result,
        ]);
    }
    #[Route('/tri-prix', name: 'tri_prix')]
    public function triPrix(Request $request, ProduitRepository $produitRepository): Response
    {
        $tri = $request->query->get('tri', 'asc'); // Par défaut, tri croissant
        $produits = $produitRepository->findBy([], ['prix_produit' => $tri]);

        return $this->render('produit/index.html.twig', [
            'produits' => $produits,
        ]);
    }
    #[Route('/total-prix', name: 'total_prix', methods: ['GET'])]
    public function totalPrix(ProduitRepository $produitRepository): JsonResponse
    {
        // Récupérer tous les produits
        $produits = $produitRepository->findAll();

        // Initialiser le total à zéro
        $total = 0;

        // Itérer sur chaque produit et ajouter son prix au total
        foreach ($produits as $produit) {
            // Assurez-vous que le prix est un nombre valide avant de l'ajouter
            if (is_numeric($produit->getPrixProduit())) {
                $total += $produit->getPrixProduit();
            }
        }

        // Retourner le total au format JSON
        return $this->json(['total' => $total]);
    }

    #[Route('/{id_produit}/edit', name: 'app_produit_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Produit $produit, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ProduitType::class, $produit);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('image_produit')->getData();
            if ($file) {
                // Generate a unique name for the file before saving it
                $fileName = md5(uniqid()).'.'.$file->guessExtension();


                // Move the file to the directory where brochures are stored
                $targetDirectory = $this->getParameter('kernel.project_dir') . '/public';
                $file->move(
                    $targetDirectory,
                    $fileName
                );
                $produit->setImageProduit($fileName);
            }
            $entityManager->flush();
            flash()->addSuccess('Product Modified Successfully');
            return $this->redirectToRoute('app_produit_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('produit/edit.html.twig', [
            'produit' => $produit,
            'form' => $form,
        ]);
    }
    #[Route('/listpf', name: 'app_produit_listpf', methods: ['GET'])]
    public function listPF(ProduitRepository $produitRepository): Response
    {
        return $this->render('produit/list.html.twig', [
            'produits' => $produitRepository->findAll(),
        ]);
    }
    #[Route('/{id_produit}', name: 'app_produit_delete')]
    public function delete(ProduitRepository  $repo,$id_produit, ManagerRegistry $mr): Response
    {

        $produit=$repo->find($id_produit);
        $em=$mr->getManager();
        $em->remove($produit);
        $em->flush();
        flash()->addWarning('Product Deleted');

        return $this->redirectToRoute('app_produit_index');
    }
}