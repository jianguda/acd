package org.toy.acd.ir;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.GenericVisitor;

public class Visitor<R, A> implements GenericVisitor<R, A> {
    @Override
    public R visit(CompilationUnit n, A arg) {
        return null;
    }

    @Override
    public R visit(PackageDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(TypeParameter n, A arg) {
        return null;
    }

    @Override
    public R visit(LineComment n, A arg) {
        return null;
    }

    @Override
    public R visit(BlockComment n, A arg) {
        return null;
    }

    @Override
    public R visit(ClassOrInterfaceDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(EnumDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(EnumConstantDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(AnnotationDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(AnnotationMemberDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(FieldDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(VariableDeclarator n, A arg) {
        return null;
    }

    @Override
    public R visit(ConstructorDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(MethodDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(Parameter n, A arg) {
        return null;
    }

    @Override
    public R visit(InitializerDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(JavadocComment n, A arg) {
        return null;
    }

    @Override
    public R visit(ClassOrInterfaceType n, A arg) {
        return null;
    }

    @Override
    public R visit(PrimitiveType n, A arg) {
        return null;
    }

    @Override
    public R visit(ArrayType n, A arg) {
        return null;
    }

    @Override
    public R visit(ArrayCreationLevel n, A arg) {
        return null;
    }

    @Override
    public R visit(IntersectionType n, A arg) {
        return null;
    }

    @Override
    public R visit(UnionType n, A arg) {
        return null;
    }

    @Override
    public R visit(VoidType n, A arg) {
        return null;
    }

    @Override
    public R visit(WildcardType n, A arg) {
        return null;
    }

    @Override
    public R visit(UnknownType n, A arg) {
        return null;
    }

    @Override
    public R visit(ArrayAccessExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ArrayCreationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ArrayInitializerExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(AssignExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(BinaryExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(CastExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ClassExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ConditionalExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(EnclosedExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(FieldAccessExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(InstanceOfExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(StringLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(IntegerLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(LongLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(CharLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(DoubleLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(BooleanLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(NullLiteralExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(MethodCallExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(NameExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ObjectCreationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(ThisExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(SuperExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(UnaryExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(VariableDeclarationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(MarkerAnnotationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(SingleMemberAnnotationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(NormalAnnotationExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(MemberValuePair n, A arg) {
        return null;
    }

    @Override
    public R visit(ExplicitConstructorInvocationStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(LocalClassDeclarationStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(AssertStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(BlockStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(LabeledStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(EmptyStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ExpressionStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(SwitchStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(SwitchEntry n, A arg) {
        return null;
    }

    @Override
    public R visit(BreakStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ReturnStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(IfStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(WhileStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ContinueStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(DoStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ForEachStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ForStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ThrowStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(SynchronizedStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(TryStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(CatchClause n, A arg) {
        return null;
    }

    @Override
    public R visit(LambdaExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(MethodReferenceExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(TypeExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(NodeList n, A arg) {
        return null;
    }

    @Override
    public R visit(Name n, A arg) {
        return null;
    }

    @Override
    public R visit(SimpleName n, A arg) {
        return null;
    }

    @Override
    public R visit(ImportDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleDeclaration n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleRequiresDirective n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleExportsDirective n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleProvidesDirective n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleUsesDirective n, A arg) {
        return null;
    }

    @Override
    public R visit(ModuleOpensDirective n, A arg) {
        return null;
    }

    @Override
    public R visit(UnparsableStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(ReceiverParameter n, A arg) {
        return null;
    }

    @Override
    public R visit(VarType n, A arg) {
        return null;
    }

    @Override
    public R visit(Modifier n, A arg) {
        return null;
    }

    @Override
    public R visit(SwitchExpr n, A arg) {
        return null;
    }

    @Override
    public R visit(YieldStmt n, A arg) {
        return null;
    }

    @Override
    public R visit(TextBlockLiteralExpr n, A arg) {
        return null;
    }
}
