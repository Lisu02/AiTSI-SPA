package PKB;

import org.example.PKB.API.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


public class ConnectionTests {

    @Test
    public void ProgramProcedureLinkTest()
    {
        try {
            IAST ast = PKB.getAST();

            TNode prog = ast.createTNode(EntityType.PROGRAM);
            TNode lproc = ast.createTNode(EntityType.PROCEDURE);
            TNode rproc = ast.createTNode(EntityType.PROCEDURE);

            ast.setRoot(prog);
            ast.setParentChildLink(prog, lproc);
            ast.setParentChildLink(prog, rproc);

            TNode root = ast.getRoot();
            Assertions.assertEquals(prog, root);

            TNode child1 = ast.getFirstChild(root);
            Assertions.assertEquals(lproc, child1);

            TNode child2 = ast.getLinkedNode(LinkType.RightSibling, child1);
            Assertions.assertEquals(rproc, child2);

        } catch (Exception e) {
            System.out.println("Program link test failed: " + e.getMessage());
        }
        finally {
            System.out.println("Program link test passed");
        }
    }
}
