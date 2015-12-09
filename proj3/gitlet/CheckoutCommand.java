/**
 * 
 */
package gitlet;

/**
 * @author william
 *
 */
public class CheckoutCommand implements Command {

    /* (non-Javadoc)
     * @see gitlet.Command#run(gitlet.Repository, java.lang.String[])
     */
    @Override
    public void run(Repository repo, String[] args) {
        if(args.length == 1)
            checkoutBranch(repo, args[0]);
        else if(args.length == 2)
            checkoutFile(repo, repo.getHead(), args[1]);
        else if(args.length == 3)
            checkoutFile(repo, args[0], args[2]);

    }
    
    /**
     * Checks out a file from a commit.
     * @param filename The file name.
     * @param commitHash The commit.
     */
    public static void checkoutFile(Repository repo, String commitHash, String filename){
        Commit toCheck;
        if(commitHash.length() == 40)
            toCheck = repo.getCommit(commitHash);
        else
            toCheck = repo.firstCommitWhere(x -> x.startsWith(commitHash));
        
        if(toCheck == null)
            throw new IllegalArgumentException("No commit with that id exists.");
        repo.checkout(toCheck, filename, false);
    }
    
    /**
     * Checks out an entire branch.
     * @param branch The branch to which to changed.
     */
    public static void checkoutBranch(Repository repo, String branch){
        if(branch.equals(repo.getBranch()))
            throw new IllegalStateException("No need to checkout the current branch.");
        
        
        String commitHash = repo.getBranchHead(branch);
        repo.checkout(repo.getCommit(commitHash));
        repo.setBranch(branch);
        repo.setHead(commitHash);
        
    }

    /* (non-Javadoc)
     * @see gitlet.Command#requiresRepo()
     */
    @Override
    public boolean requiresRepo() {
        return true;
    }

    /* (non-Javadoc)
     * @see gitlet.Command#checkOperands(java.lang.String[])
     */
    @Override
    public boolean checkOperands(String[] args) {
        // TODO Auto-generated method stub
        return (args.length == 1 && !args[0].equals("--"))
                || (args.length == 3 && args[1].equals("--")
                || (args.length == 2 && args[0].equals("--")));
    }

}