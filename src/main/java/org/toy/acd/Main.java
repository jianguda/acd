package org.toy.acd;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.toy.acd.cfg.*;
import org.toy.acd.ir.CFG;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The main entrypoint for the compiler.  Consists of a series
 * of routines which must be invoked in order.  The main()
 * routine here invokes these routines, as does the unit testing
 * code. This is not the <b>best</b> programming practice, as the
 * series of calls to be invoked is duplicated in two places in the
 * code, but it will do for now.
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        ClassLoader classLoader = main.getClass().getClassLoader();

        String fileName = "acd/demo.java";
        File file = new File(classLoader.getResource(fileName).getFile());
        main.process(file);
    }

    /**
     * @param file The java file to test.
     */
    public void process(File file) {
        try {
            FileReader fr = new FileReader(file);
            CompilationUnit compilationUnit = StaticJavaParser.parse(fr);
            fr.close();

            Map<MethodDeclaration, CFG> mdCFG = new HashMap<>();

            // Code -> AST
            ClassOrInterfaceDeclaration cls = compilationUnit.getClassByName("Main").orElse(null);
            List<MethodDeclaration> mds = cls.getMethods();

            // Build CFG
            for (MethodDeclaration md : mds) {
                CFG cfg = new CFGBuilder().run(md);
                mdCFG.put(md, cfg);
            }

            // Compute dominators
            for (MethodDeclaration md : mds)
                new Dominator().run(mdCFG.get(md));

            // Construct SSA
            for (MethodDeclaration md : mds)
                new SSA().run(mdCFG.get(md));

            // Apply optimizations
            for (MethodDeclaration md : mds)
                new Optimizer().run(mdCFG.get(md));

            // Destruct SSA
            for (MethodDeclaration md : mds)
                new DeSSA().run(mdCFG.get(md));

            // AST -> Code
            File optFile = new File(file.getAbsolutePath().replace(".java", ".opt.java"));
            FileWriter fw = new FileWriter(optFile);
            fw.write(cls.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
